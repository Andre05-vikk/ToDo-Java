package ee.taltech.todo.repository;

import ee.taltech.todo.model.Category;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TaskRepository using ConcurrentHashMap.
 *
 * This implementation stores tasks in memory and provides thread-safe operations.
 * Suitable for development, testing, and applications that don't require persistence.
 *
 * Design Pattern: Repository Pattern (implementation)
 * Thread Safety: Uses ConcurrentHashMap for thread-safe operations
 *
 * @author ToDo Application
 * @version 1.0
 */
public class InMemoryTaskRepository implements TaskRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryTaskRepository.class);

    /**
     * Thread-safe storage for tasks.
     * Key: Task ID, Value: Task object
     */
    private final Map<String, Task> storage;

    /**
     * Default constructor initializing the storage.
     */
    public InMemoryTaskRepository() {
        this.storage = new ConcurrentHashMap<>();
        logger.info("InMemoryTaskRepository initialized");
    }

    @Override
    public Task save(Task task) {
        if (task == null) {
            logger.error("Attempted to save null task");
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (task.getId() == null || task.getId().isEmpty()) {
            logger.error("Attempted to save task with null/empty ID");
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }

        boolean isUpdate = storage.containsKey(task.getId());
        storage.put(task.getId(), task);

        if (isUpdate) {
            logger.debug("Updated task: {}", task.getId());
        } else {
            logger.debug("Created new task: {}", task.getId());
        }

        return task;
    }

    @Override
    public Optional<Task> findById(String id) {
        if (id == null) {
            logger.error("Attempted to find task with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        Task task = storage.get(id);
        logger.debug("Find task by ID {}: {}", id, task != null ? "found" : "not found");
        return Optional.ofNullable(task);
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>(storage.values());
        logger.debug("Retrieved all tasks: {} found", tasks.size());
        return tasks;
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null) {
            logger.error("Attempted to delete task with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        Task removed = storage.remove(id);
        boolean deleted = removed != null;
        logger.debug("Delete task by ID {}: {}", id, deleted ? "success" : "not found");
        return deleted;
    }

    @Override
    public boolean delete(Task task) {
        if (task == null) {
            logger.error("Attempted to delete null task");
            throw new IllegalArgumentException("Task cannot be null");
        }

        return deleteById(task.getId());
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) {
            logger.error("Attempted to check existence with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        boolean exists = storage.containsKey(id);
        logger.debug("Check task exists by ID {}: {}", id, exists);
        return exists;
    }

    @Override
    public long count() {
        long count = storage.size();
        logger.debug("Total task count: {}", count);
        return count;
    }

    @Override
    public void deleteAll() {
        int previousCount = storage.size();
        storage.clear();
        logger.info("Deleted all tasks (previous count: {})", previousCount);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        if (status == null) {
            logger.warn("findByStatus called with null status, returning empty list");
            return Collections.emptyList();
        }

        List<Task> tasks = storage.values().stream()
                .filter(task -> status.equals(task.getStatus()))
                .collect(Collectors.toList());

        logger.debug("Found {} tasks with status {}", tasks.size(), status);
        return tasks;
    }

    @Override
    public List<Task> findByPriority(TaskPriority priority) {
        if (priority == null) {
            logger.warn("findByPriority called with null priority, returning empty list");
            return Collections.emptyList();
        }

        List<Task> tasks = storage.values().stream()
                .filter(task -> priority.equals(task.getPriority()))
                .collect(Collectors.toList());

        logger.debug("Found {} tasks with priority {}", tasks.size(), priority);
        return tasks;
    }

    @Override
    public List<Task> findByCategory(Category category) {
        if (category == null) {
            logger.warn("findByCategory called with null category, returning empty list");
            return Collections.emptyList();
        }

        List<Task> tasks = storage.values().stream()
                .filter(task -> task.getCategory() != null &&
                               category.getId().equals(task.getCategory().getId()))
                .collect(Collectors.toList());

        logger.debug("Found {} tasks in category {}", tasks.size(), category.getName());
        return tasks;
    }

    @Override
    public List<Task> findStarred() {
        List<Task> tasks = storage.values().stream()
                .filter(Task::isStarred)
                .collect(Collectors.toList());

        logger.debug("Found {} starred tasks", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> findOverdue() {
        List<Task> tasks = storage.values().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());

        logger.debug("Found {} overdue tasks", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            logger.warn("findByDueDateBetween called with null dates, returning empty list");
            return Collections.emptyList();
        }

        List<Task> tasks = storage.values().stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> !task.getDueDate().isBefore(start) &&
                               !task.getDueDate().isAfter(end))
                .collect(Collectors.toList());

        logger.debug("Found {} tasks due between {} and {}", tasks.size(), start, end);
        return tasks;
    }

    @Override
    public List<Task> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            logger.warn("searchByTitle called with empty keyword, returning all tasks");
            return findAll();
        }

        String lowerKeyword = keyword.toLowerCase();
        List<Task> tasks = storage.values().stream()
                .filter(task -> task.getTitle() != null &&
                               task.getTitle().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());

        logger.debug("Found {} tasks matching keyword '{}'", tasks.size(), keyword);
        return tasks;
    }
}
