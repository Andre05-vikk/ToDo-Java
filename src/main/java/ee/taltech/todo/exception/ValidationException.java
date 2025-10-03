package ee.taltech.todo.exception;

/**
 * Exception thrown when input validation fails.
 *
 * Used to indicate that user input or data does not meet validation requirements.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class ValidationException extends Exception {

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message The detail message explaining the validation failure
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause   The cause of this exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
