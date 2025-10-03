package ee.taltech.todo.exception;

/**
 * Exception thrown when a requested task is not found.
 *
 * This is a custom checked exception that provides meaningful error messages
 * when tasks cannot be located in the repository.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class TaskNotFoundException extends Exception {

    /**
     * Constructs a new TaskNotFoundException with the specified detail message.
     *
     * @param message The detail message
     */
    public TaskNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new TaskNotFoundException with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause   The cause of this exception
     */
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a TaskNotFoundException for a task with specific ID.
     *
     * @param taskId The ID of the task that was not found
     * @return A new TaskNotFoundException with formatted message
     */
    public static TaskNotFoundException forId(String taskId) {
        return new TaskNotFoundException("Task not found with ID: " + taskId);
    }
}
