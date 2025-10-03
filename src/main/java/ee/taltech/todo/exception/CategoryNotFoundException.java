package ee.taltech.todo.exception;

/**
 * Exception thrown when a requested category is not found.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class CategoryNotFoundException extends Exception {

    /**
     * Constructs a new CategoryNotFoundException with the specified detail message.
     *
     * @param message The detail message
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new CategoryNotFoundException with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause   The cause of this exception
     */
    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a CategoryNotFoundException for a category with specific ID.
     *
     * @param categoryId The ID of the category that was not found
     * @return A new CategoryNotFoundException with formatted message
     */
    public static CategoryNotFoundException forId(String categoryId) {
        return new CategoryNotFoundException("Category not found with ID: " + categoryId);
    }

    /**
     * Constructs a CategoryNotFoundException for a category with specific name.
     *
     * @param categoryName The name of the category that was not found
     * @return A new CategoryNotFoundException with formatted message
     */
    public static CategoryNotFoundException forName(String categoryName) {
        return new CategoryNotFoundException("Category not found with name: " + categoryName);
    }
}
