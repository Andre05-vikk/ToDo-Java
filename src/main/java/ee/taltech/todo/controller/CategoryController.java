package ee.taltech.todo.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ee.taltech.todo.dto.CategoryDTO;
import ee.taltech.todo.dto.CreateCategoryRequest;
import ee.taltech.todo.exception.CategoryNotFoundException;
import ee.taltech.todo.exception.DuplicateEntityException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;
import ee.taltech.todo.service.CategoryService;
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
 * REST API controller for Category operations.
 *
 * Handles HTTP requests for category management.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class CategoryController implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    /**
     * Constructor with dependency injection.
     *
     * @param categoryService The category service
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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
            if (path.matches("/api/v1/categories/?$")) {
                handleCategoriesEndpoint(exchange, method);
            } else if (path.matches("/api/v1/categories/[^/]+/?$")) {
                String categoryId = extractId(path);
                handleCategoryByIdEndpoint(exchange, method, categoryId);
            } else {
                sendError(exchange, 404, "Endpoint not found");
            }
        } catch (Exception e) {
            logger.error("Error handling request", e);
            sendError(exchange, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void handleCategoriesEndpoint(HttpExchange exchange, String method) throws IOException {
        switch (method) {
            case "GET":
                handleGetAllCategories(exchange);
                break;
            case "POST":
                handleCreateCategory(exchange);
                break;
            default:
                sendError(exchange, 405, "Method not allowed");
        }
    }

    private void handleCategoryByIdEndpoint(HttpExchange exchange, String method, String categoryId) throws IOException {
        switch (method) {
            case "GET":
                handleGetCategory(exchange, categoryId);
                break;
            case "PUT":
                handleUpdateCategory(exchange, categoryId);
                break;
            case "DELETE":
                handleDeleteCategory(exchange, categoryId);
                break;
            default:
                sendError(exchange, 405, "Method not allowed");
        }
    }

    private void handleGetAllCategories(HttpExchange exchange) throws IOException {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> dtos = categories.stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());

        sendJsonResponse(exchange, 200, dtos);
    }

    private void handleCreateCategory(HttpExchange exchange) throws IOException {
        try {
            String body = readRequestBody(exchange);
            CreateCategoryRequest request = JsonUtil.fromJson(body, CreateCategoryRequest.class);

            Category category = request.toEntity();
            Category created = categoryService.createCategory(category);
            CategoryDTO dto = CategoryDTO.fromEntity(created);

            sendJsonResponse(exchange, 201, dto);

        } catch (ValidationException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (DuplicateEntityException e) {
            sendError(exchange, 409, e.getMessage());
        }
    }

    private void handleGetCategory(HttpExchange exchange, String categoryId) throws IOException {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            CategoryDTO dto = CategoryDTO.fromEntity(category);
            sendJsonResponse(exchange, 200, dto);
        } catch (CategoryNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private void handleUpdateCategory(HttpExchange exchange, String categoryId) throws IOException {
        try {
            String body = readRequestBody(exchange);
            CreateCategoryRequest request = JsonUtil.fromJson(body, CreateCategoryRequest.class);

            Category category = categoryService.getCategoryById(categoryId);

            // Update fields if provided
            if (request.getName() != null) category.setName(request.getName());
            if (request.getDescription() != null) category.setDescription(request.getDescription());
            if (request.getColor() != null) category.setColor(request.getColor());

            Category updated = categoryService.updateCategory(category);
            CategoryDTO dto = CategoryDTO.fromEntity(updated);

            sendJsonResponse(exchange, 200, dto);

        } catch (CategoryNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        } catch (ValidationException e) {
            sendError(exchange, 400, e.getMessage());
        } catch (DuplicateEntityException e) {
            sendError(exchange, 409, e.getMessage());
        }
    }

    private void handleDeleteCategory(HttpExchange exchange, String categoryId) throws IOException {
        try {
            categoryService.deleteCategory(categoryId);
            sendJsonResponse(exchange, 204, null);
        } catch (CategoryNotFoundException e) {
            sendError(exchange, 404, e.getMessage());
        }
    }

    private String extractId(String path) {
        String[] parts = path.split("/");
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
