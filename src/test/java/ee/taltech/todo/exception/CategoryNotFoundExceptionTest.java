package ee.taltech.todo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CategoryNotFoundException.
 */
class CategoryNotFoundExceptionTest {

    @Test
    void testConstructor_WithMessage() {
        String message = "Category not found";
        CategoryNotFoundException exception = new CategoryNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructor_WithMessageAndCause() {
        String message = "Category not found";
        Throwable cause = new RuntimeException("Root cause");
        CategoryNotFoundException exception = new CategoryNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testForId_ShouldCreateExceptionWithFormattedMessage() {
        String categoryId = "cat-123";
        CategoryNotFoundException exception = CategoryNotFoundException.forId(categoryId);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(categoryId));
        assertTrue(exception.getMessage().contains("Category not found with ID"));
    }

    @Test
    void testForName_ShouldCreateExceptionWithFormattedMessage() {
        String categoryName = "Work";
        CategoryNotFoundException exception = CategoryNotFoundException.forName(categoryName);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(categoryName));
        assertTrue(exception.getMessage().contains("Category not found with name"));
    }

    @Test
    void testExceptionIsCheckedException() {
        assertTrue(Exception.class.isAssignableFrom(CategoryNotFoundException.class));
    }

    @Test
    void testThrowAndCatch() {
        assertThrows(CategoryNotFoundException.class, () -> {
            throw new CategoryNotFoundException("Test exception");
        });
    }
}
