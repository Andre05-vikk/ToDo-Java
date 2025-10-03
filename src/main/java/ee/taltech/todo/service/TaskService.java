package ee.taltech.todo.service;

import ee.taltech.todo.exception.TaskNotFoundException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for task business logic operations.
 *
 * This interface defines the business operations for managing tasks.
 * It provides a layer of abstraction between controllers and repositories.
 *
 * @author ToDo Application
 * @version 1.0
 */
public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param task The task to create
     * @return The created task
     * @throws ValidationException if task validation fails
     */
    Task createTask(Task task) throws ValidationException;

    /**
     * Updates an existing task.
     *
     * @param task The task with updated information
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     * @throws ValidationException   if task validation fails
     */
    Task updateTask(Task task) throws TaskNotFoundException, ValidationException;

    /**
     * Finds a task by its ID.
     *
     * @param id The task ID
     * @return The task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task getTaskById(String id) throws TaskNotFoundException;

    /**
     * Retrieves all tasks.
     *
     * @return List of all tasks
     */
    List<Task> getAllTasks();

    /**
     * Deletes a task by its ID.
     *
     * @param id The task ID
     * @throws TaskNotFoundException if task doesn't exist
     */
    void deleteTask(String id) throws TaskNotFoundException;

    /**
     * Marks a task as completed.
     *
     * @param id The task ID
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task completeTask(String id) throws TaskNotFoundException;

    /**
     * Starts a task (changes status to IN_PROGRESS).
     *
     * @param id The task ID
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task startTask(String id) throws TaskNotFoundException;

    /**
     * Cancels a task.
     *
     * @param id The task ID
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task cancelTask(String id) throws TaskNotFoundException;

    /**
     * Toggles the starred status of a task.
     *
     * @param id The task ID
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task toggleStarred(String id) throws TaskNotFoundException;

    /**
     * Sets a task's priority.
     *
     * @param id       The task ID
     * @param priority The new priority
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task setPriority(String id, TaskPriority priority) throws TaskNotFoundException;

    /**
     * Sets a task's due date.
     *
     * @param id      The task ID
     * @param dueDate The due date
     * @return The updated task
     * @throws TaskNotFoundException if task doesn't exist
     */
    Task setDueDate(String id, LocalDateTime dueDate) throws TaskNotFoundException;

    /**
     * Assigns a category to a task.
     *
     * @param taskId     The task ID
     * @param categoryId The category ID
     * @return The updated task
     * @throws TaskNotFoundException if task or category doesn't exist
     */
    Task assignCategory(String taskId, String categoryId) throws TaskNotFoundException;

    /**
     * Finds tasks by status.
     *
     * @param status The status to filter by
     * @return List of tasks with the given status
     */
    List<Task> getTasksByStatus(TaskStatus status);

    /**
     * Finds tasks by priority.
     *
     * @param priority The priority to filter by
     * @return List of tasks with the given priority
     */
    List<Task> getTasksByPriority(TaskPriority priority);

    /**
     * Finds tasks by category ID.
     *
     * @param categoryId The category ID
     * @return List of tasks in the category
     */
    List<Task> getTasksByCategory(String categoryId);

    /**
     * Finds all starred tasks.
     *
     * @return List of starred tasks
     */
    List<Task> getStarredTasks();

    /**
     * Finds all overdue tasks.
     *
     * @return List of overdue tasks
     */
    List<Task> getOverdueTasks();

    /**
     * Searches tasks by title keyword.
     *
     * @param keyword The search keyword
     * @return List of matching tasks
     */
    List<Task> searchTasks(String keyword);

    /**
     * Gets count of tasks by status.
     *
     * @param status The status
     * @return Number of tasks with that status
     */
    long countByStatus(TaskStatus status);

    /**
     * Gets total task count.
     *
     * @return Total number of tasks
     */
    long getTotalCount();
}
