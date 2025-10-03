package ee.taltech.todo.repository;

import ee.taltech.todo.model.Category;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Task entity operations.
 *
 * Extends the generic Repository interface and adds task-specific query methods.
 * This demonstrates interface extension and specialized data access patterns.
 *
 * @author ToDo Application
 * @version 1.0
 */
public interface TaskRepository extends Repository<Task> {

    /**
     * Finds all tasks with a specific status.
     *
     * @param status The status to filter by
     * @return List of tasks with the given status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Finds all tasks with a specific priority.
     *
     * @param priority The priority to filter by
     * @return List of tasks with the given priority
     */
    List<Task> findByPriority(TaskPriority priority);

    /**
     * Finds all tasks in a specific category.
     *
     * @param category The category to filter by
     * @return List of tasks in the given category
     */
    List<Task> findByCategory(Category category);

    /**
     * Finds all tasks that are marked as starred/favorite.
     *
     * @return List of starred tasks
     */
    List<Task> findStarred();

    /**
     * Finds all tasks that are overdue (past due date and not completed).
     *
     * @return List of overdue tasks
     */
    List<Task> findOverdue();

    /**
     * Finds tasks with due date within a specific date range.
     *
     * @param start Start of date range (inclusive)
     * @param end   End of date range (inclusive)
     * @return List of tasks due within the range
     */
    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Searches tasks by title (case-insensitive partial match).
     *
     * @param keyword The keyword to search for in titles
     * @return List of tasks matching the keyword
     */
    List<Task> searchByTitle(String keyword);

    /**
     * Finds all completed tasks.
     * Convenience method equivalent to findByStatus(TaskStatus.COMPLETED).
     *
     * @return List of completed tasks
     */
    default List<Task> findCompleted() {
        return findByStatus(TaskStatus.COMPLETED);
    }

    /**
     * Finds all pending tasks.
     * Convenience method equivalent to findByStatus(TaskStatus.PENDING).
     *
     * @return List of pending tasks
     */
    default List<Task> findPending() {
        return findByStatus(TaskStatus.PENDING);
    }

    /**
     * Finds all in-progress tasks.
     * Convenience method equivalent to findByStatus(TaskStatus.IN_PROGRESS).
     *
     * @return List of in-progress tasks
     */
    default List<Task> findInProgress() {
        return findByStatus(TaskStatus.IN_PROGRESS);
    }
}
