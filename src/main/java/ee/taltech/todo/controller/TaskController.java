package ee.taltech.todo.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ee.taltech.todo.dto.CreateTaskRequest;
import ee.taltech.todo.dto.TaskDTO;
import ee.taltech.todo.dto.UpdateTaskRequest;
import ee.taltech.todo.exception.TaskNotFoundException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import ee.taltech.todo.service.TaskService;
import ee.taltech.todo.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API controller for Task operations.
 *
 * Handles HTTP requests for task management.
 * Provides RESTful endpoints for CRUD operations and task queries.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class TaskController implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    /**
     * Constructor with dependency injection.
     *
     * @param taskService The task service
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Add CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // Handle preflight OPTIONS request
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        logger.debug("Handling {} request to {}", method, path);

        try {
            if (path.matches("/api/v1/tasks/?$")) {
                handleTasksEndpoint(exchange, method);
            } else if (path.matches("/api/v1/tasks/[^/]+/?$")) {
                String taskId = extractId(path);
                handleTaskByIdEndpoint(exchange, method, taskId);
            } else if (path.matches("/api/v1/tasks/status/[^/]+/?$")) {
                String statusStr = extractLastSegment(path);
                handleTasksByStatus(exchange, statusStr);
            } else if (path.matches("/api/v1/tasks/priority/[^/]+/?$")) {
                String priorityStr = extractLastSegment(path);
                handleTasksByPriority(exchange, priorityStr);
            } else if (path.matches("/api/v1/tasks/[^/]+/complete/?$")) {
                String taskId = extractId(path);
                handleCompleteTask(exchange, taskId);
            } else if (path.matches("/api/v1/tasks/[^/]+/start/?$")) {
                String taskId = extractId(path);
                handleStartTask(exchange, taskId);
            } else if (path.matches("/api/v1/tasks/starred/?$")) {
                handleStarredTasks(exchange);
            } else if (path.matches("/api/v1/tasks/overdue/?$")) {
                handleOverdueTasks(exchange);
            } else {
                sendError(exchange, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            logger.error("Error handling request", e);
            sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleTasksEndpoint(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET":
                handleGetAllTasks(exchange);
                break;
            case "POST":
                handleCreateTask(exchange);
                break;
            default:
                sendError(exchange, 405, "Method not allowed");
        }
    }

    private void handleTaskByIdEndpoint(HttpExchange exchange, String method, String taskId) throws IOException {
        switch (method) {
            case "GET":
                handleGetTask(exchange, taskId);
                break;
            case "PUT":
                handleUpdateTask(exchange, taskId);
                break;
            case "DELETE":
                handleDeleteTask(exchange, taskId);
                break;
            default:
                sendError(exchange, 405, "Method not allowed");
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskDTO> dtos = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());

        sendJsonResponse(exchange, 200, dtos);
    }

    private void handleCreateTask(HttpExchange exchange) throws IOException {
        try {
            String body = readRequestBody(exchange);
            CreateTaskRequest request = JsonUtil.fromJson(body, CreateTaskRequest.class);

            Task task = request.toEntity();

            // Assign category if provided
            if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
                task = taskService.assignCategory(task.getId(), request.getCategoryId());
            }

            Task created = taskService.createTask(task);
            TaskDTO dto = TaskDTO.fromEntity(created);

            sendJsonResponse(exchange, 201, dto);

        } catch (ValidationException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private void handleGetTask(HttpExchange exchange, String taskId) throws IOException {
        try {
            Task task = taskService.getTaskById(taskId);
            TaskDTO dto = TaskDTO.fromEntity(task);
            sendJsonResponse(exchange, 200, dto);
        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private void handleUpdateTask(HttpExchange exchange, String taskId) throws IOException {
        try {
            String body = readRequestBody(exchange);
            UpdateTaskRequest request = JsonUtil.fromJson(body, UpdateTaskRequest.class);

            Task task = taskService.getTaskById(taskId);

            // Update fields if provided
            if (request.getTitle() != null) task.setTitle(request.getTitle());
            if (request.getDescription() != null) task.setDescription(request.getDescription());
            if (request.getStatus() != null) task.setStatus(request.getStatus());
            if (request.getPriority() != null) task.setPriority(request.getPriority());
            if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
            if (request.getStarred() != null) task.setStarred(request.getStarred());

            Task updated = taskService.updateTask(task);

            // Update category if provided
            if (request.getCategoryId() != null && !request.getCategoryId().isEmpty()) {
                updated = taskService.assignCategory(taskId, request.getCategoryId());
            }

            TaskDTO dto = TaskDTO.fromEntity(updated);
            sendJsonResponse(exchange, 200, dto);

        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        } catch (ValidationException e) {
            sendError(exchange, 400, e.getMessage());
        }
    }

    private void handleDeleteTask(HttpExchange exchange, String taskId) throws IOException {
        try {
            taskService.deleteTask(taskId);
            sendJsonResponse(exchange, 204, null);
        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private void handleTasksByStatus(HttpExchange exchange, String statusStr) throws IOException {
        try {
            TaskStatus status = TaskStatus.valueOf(statusStr.toUpperCase());
            List<Task> tasks = taskService.getTasksByStatus(status);
            List<TaskDTO> dtos = tasks.stream()
                    .map(TaskDTO::fromEntity)
                    .collect(Collectors.toList());
            sendJsonResponse(exchange, 200, dtos);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, "Invalid status: " + statusStr);
        }
    }

    private void handleTasksByPriority(HttpExchange exchange, String priorityStr) throws IOException {
        try {
            TaskPriority priority = TaskPriority.valueOf(priorityStr.toUpperCase());
            List<Task> tasks = taskService.getTasksByPriority(priority);
            List<TaskDTO> dtos = tasks.stream()
                    .map(TaskDTO::fromEntity)
                    .collect(Collectors.toList());
            sendJsonResponse(exchange, 200, dtos);
        } catch (IllegalArgumentException e) {
            sendError(exchange, 400, "Invalid priority: " + priorityStr);
        }
    }

    private void handleCompleteTask(HttpExchange exchange, String taskId) throws IOException {
        try {
            Task task = taskService.completeTask(taskId);
            TaskDTO dto = TaskDTO.fromEntity(task);
            sendJsonResponse(exchange, 200, dto);
        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private void handleStartTask(HttpExchange exchange, String taskId) throws IOException {
        try {
            Task task = taskService.startTask(taskId);
            TaskDTO dto = TaskDTO.fromEntity(task);
            sendJsonResponse(exchange, 200, dto);
        } catch (TaskNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private void handleStarredTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskService.getStarredTasks();
        List<TaskDTO> dtos = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
        sendJsonResponse(exchange, 200, dtos);
    }

    private void handleOverdueTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskService.getOverdueTasks();
        List<TaskDTO> dtos = tasks.stream()
                .map(TaskDTO::fromEntity)
                .collect(Collectors.toList());
        sendJsonResponse(exchange, 200, dtos);
    }

    private String extractId(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

    private String extractLastSegment(String path) {
        String[] parts = path.replaceAll("/$", "").split("/");
        return parts[parts.length - 1];
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, Object data) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        String response = data != null ? JsonUtil.toJson(data) : "";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(statusCode, bytes.length);
        if (bytes.length > 0) {
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");

        String error = String.format("{\"error\": \"%s\", \"status\": %d}", message, statusCode);
        byte[] bytes = error.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
