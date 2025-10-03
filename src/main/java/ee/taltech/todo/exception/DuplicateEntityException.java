package ee.taltech.todo.exception;

/**
 * Exception thrown when attempting to create an entity that already exists.
 *
 * Used to prevent duplicate entries in the system (e.g., duplicate category names).
 *
 * @author ToDo Application
 * @version 1.0
 */
public class DuplicateEntityException extends Exception {

    /**
     * Constructs a new DuplicateEntityException with the specified detail message.
     *
     * @param message The detail message
     */
    public DuplicateEntityException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateEntityException with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause   The cause of this exception
     */
    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a DuplicateEntityException for a specific entity type and identifier.
     *
     * @param entityType The type of entity (e.g., "Category", "Task")
     * @param identifier The identifier that is duplicate
     * @return A new DuplicateEntityException with formatted message
     */
    public static DuplicateEntityException forEntity(String entityType, String identifier) {
        return new DuplicateEntityException(entityType + " already exists: " + identifier);
    }
}
