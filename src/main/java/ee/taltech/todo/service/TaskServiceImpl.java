package ee.taltech.todo.service;

import ee.taltech.todo.exception.TaskNotFoundException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import ee.taltech.todo.repository.CategoryRepository;
import ee.taltech.todo.repository.TaskRepository;
import ee.taltech.todo.validator.TaskValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of TaskService interface.
 *
 * Demonstrates OOP Composition: TaskService HAS-A TaskRepository and CategoryRepository.
 * This service layer contains all business logic for task management.
 *
 * Design Patterns:
 * - Composition: Service has-a Repository
 * - Dependency Injection: Repositories injected via constructor
 * - Strategy Pattern: Uses TaskValidator for validation
 *
 * @author ToDo Application
 * @version 1.0
 */
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * Composition: TaskService HAS-A TaskRepository.
     */
    private final TaskRepository taskRepository;

    /**
     * Composition: TaskService HAS-A CategoryRepository.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Strategy Pattern: TaskValidator for validation logic.
     */
    private final TaskValidator taskValidator;

    /**
     * Constructor with dependency injection.
     *
     * @param taskRepository     The task repository
     * @param categoryRepository The category repository
     */
    public TaskServiceImpl(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = Objects.requireNonNull(taskRepository, "TaskRepository cannot be null");
        this.categoryRepository = Objects.requireNonNull(categoryRepository, "CategoryRepository cannot be null");
        this.taskValidator = new TaskValidator();
        logger.info("TaskServiceImpl initialized");
    }

    @Override
    public Task createTask(Task task) throws ValidationException {
        logger.debug("Creating new task: {}", task != null ? task.getTitle() : "null");

        validateTask(task);

        Task savedTask = taskRepository.save(task);
        logger.info("Task created successfully: ID={}, Title={}", savedTask.getId(), savedTask.getTitle());

        return savedTask;
    }

    @Override
    public Task updateTask(Task task) throws TaskNotFoundException, ValidationException {
        logger.debug("Updating task: {}", task != null ? task.getId() : "null");

        validateTask(task);

        // Check if task exists
        if (!taskRepository.existsById(task.getId())) {
            logger.warn("Attempted to update non-existent task: {}", task.getId());
            throw TaskNotFoundException.forId(task.getId());
        }

        Task updatedTask = taskRepository.save(task);
        logger.info("Task updated successfully: ID={}, Title={}", updatedTask.getId(), updatedTask.getTitle());

        return updatedTask;
    }

    @Override
    public Task getTaskById(String id) throws TaskNotFoundException {
        logger.debug("Fetching task by ID: {}", id);

        if (id == null || id.trim().isEmpty()) {
            logger.error("getTaskById called with null or empty ID");
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }

        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Task not found: {}", id);
                    return TaskNotFoundException.forId(id);
                });
    }

    @Override
    public List<Task> getAllTasks() {
        logger.debug("Fetching all tasks");
        List<Task> tasks = taskRepository.findAll();
        logger.debug("Retrieved {} tasks", tasks.size());
        return tasks;
    }

    @Override
    public void deleteTask(String id) throws TaskNotFoundException {
        logger.debug("Deleting task: {}", id);

        if (id == null || id.trim().isEmpty()) {
            logger.error("deleteTask called with null or empty ID");
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }

        // Check if task exists before deleting
        if (!taskRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent task: {}", id);
            throw TaskNotFoundException.forId(id);
        }

        taskRepository.deleteById(id);
        logger.info("Task deleted successfully: {}", id);
    }

    @Override
    public Task completeTask(String id) throws TaskNotFoundException {
        logger.debug("Completing task: {}", id);

        Task task = getTaskById(id);
        task.complete();
        Task updatedTask = taskRepository.save(task);

        logger.info("Task completed: ID={}, Title={}", updatedTask.getId(), updatedTask.getTitle());
        return updatedTask;
    }

    @Override
    public Task startTask(String id) throws TaskNotFoundException {
        logger.debug("Starting task: {}", id);

        Task task = getTaskById(id);
        task.start();
        Task updatedTask = taskRepository.save(task);

        logger.info("Task started: ID={}, Title={}", updatedTask.getId(), updatedTask.getTitle());
        return updatedTask;
    }

    @Override
    public Task cancelTask(String id) throws TaskNotFoundException {
        logger.debug("Cancelling task: {}", id);

        Task task = getTaskById(id);
        task.cancel();
        Task updatedTask = taskRepository.save(task);

        logger.info("Task cancelled: ID={}, Title={}", updatedTask.getId(), updatedTask.getTitle());
        return updatedTask;
    }

    @Override
    public Task toggleStarred(String id) throws TaskNotFoundException {
        logger.debug("Toggling starred status for task: {}", id);

        Task task = getTaskById(id);
        task.toggleStarred();
        Task updatedTask = taskRepository.save(task);

        logger.info("Task starred status toggled: ID={}, Starred={}", updatedTask.getId(), updatedTask.isStarred());
        return updatedTask;
    }

    @Override
    public Task setPriority(String id, TaskPriority priority) throws TaskNotFoundException {
        logger.debug("Setting priority for task {}: {}", id, priority);

        if (priority == null) {
            logger.error("setPriority called with null priority");
            throw new IllegalArgumentException("Priority cannot be null");
        }

        Task task = getTaskById(id);
        task.setPriority(priority);
        Task updatedTask = taskRepository.save(task);

        logger.info("Task priority updated: ID={}, Priority={}", updatedTask.getId(), priority);
        return updatedTask;
    }

    @Override
    public Task setDueDate(String id, LocalDateTime dueDate) throws TaskNotFoundException {
        logger.debug("Setting due date for task {}: {}", id, dueDate);

        Task task = getTaskById(id);
        task.setDueDate(dueDate);
        Task updatedTask = taskRepository.save(task);

        logger.info("Task due date updated: ID={}, DueDate={}", updatedTask.getId(), dueDate);
        return updatedTask;
    }

    @Override
    public Task assignCategory(String taskId, String categoryId) throws TaskNotFoundException {
        logger.debug("Assigning category {} to task {}", categoryId, taskId);

        // Validate inputs
        if (taskId == null || taskId.trim().isEmpty()) {
            logger.error("assignCategory called with null or empty task ID");
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }

        if (categoryId == null || categoryId.trim().isEmpty()) {
            logger.error("assignCategory called with null or empty category ID");
            throw new IllegalArgumentException("Category ID cannot be null or empty");
        }

        // Get task
        Task task = getTaskById(taskId);

        // Get category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.warn("Category not found: {}", categoryId);
                    return new TaskNotFoundException("Category not found with ID: " + categoryId);
                });

        task.setCategory(category);
        Task updatedTask = taskRepository.save(task);

        logger.info("Category assigned to task: TaskID={}, CategoryID={}, CategoryName={}",
                taskId, categoryId, category.getName());
        return updatedTask;
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        logger.debug("Fetching tasks by status: {}", status);

        if (status == null) {
            logger.warn("getTasksByStatus called with null status, returning all tasks");
            return getAllTasks();
        }

        List<Task> tasks = taskRepository.findByStatus(status);
        logger.debug("Found {} tasks with status {}", tasks.size(), status);
        return tasks;
    }

    @Override
    public List<Task> getTasksByPriority(TaskPriority priority) {
        logger.debug("Fetching tasks by priority: {}", priority);

        if (priority == null) {
            logger.warn("getTasksByPriority called with null priority, returning all tasks");
            return getAllTasks();
        }

        List<Task> tasks = taskRepository.findByPriority(priority);
        logger.debug("Found {} tasks with priority {}", tasks.size(), priority);
        return tasks;
    }

    @Override
    public List<Task> getTasksByCategory(String categoryId) {
        logger.debug("Fetching tasks by category: {}", categoryId);

        if (categoryId == null || categoryId.trim().isEmpty()) {
            logger.warn("getTasksByCategory called with null or empty category ID");
            return getAllTasks();
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            logger.warn("Category not found: {}, returning empty list", categoryId);
            return List.of();
        }

        List<Task> tasks = taskRepository.findByCategory(category);
        logger.debug("Found {} tasks in category {}", tasks.size(), category.getName());
        return tasks;
    }

    @Override
    public List<Task> getStarredTasks() {
        logger.debug("Fetching starred tasks");
        List<Task> tasks = taskRepository.findStarred();
        logger.debug("Found {} starred tasks", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> getOverdueTasks() {
        logger.debug("Fetching overdue tasks");
        List<Task> tasks = taskRepository.findOverdue();
        logger.debug("Found {} overdue tasks", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> searchTasks(String keyword) {
        logger.debug("Searching tasks with keyword: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            logger.warn("searchTasks called with empty keyword, returning all tasks");
            return getAllTasks();
        }

        List<Task> tasks = taskRepository.searchByTitle(keyword);
        logger.debug("Found {} tasks matching '{}'", tasks.size(), keyword);
        return tasks;
    }

    @Override
    public long countByStatus(TaskStatus status) {
        logger.debug("Counting tasks by status: {}", status);

        if (status == null) {
            logger.warn("countByStatus called with null status");
            return 0;
        }

        long count = taskRepository.findByStatus(status).size();
        logger.debug("Count of tasks with status {}: {}", status, count);
        return count;
    }

    @Override
    public long getTotalCount() {
        logger.debug("Getting total task count");
        long count = taskRepository.count();
        logger.debug("Total task count: {}", count);
        return count;
    }

    /**
     * Validates a task before saving or updating.
     * Uses TaskValidator (Strategy Pattern) for validation.
     *
     * @param task The task to validate
     * @throws ValidationException if validation fails
     */
    private void validateTask(Task task) throws ValidationException {
        taskValidator.validate(task);
    }
}
