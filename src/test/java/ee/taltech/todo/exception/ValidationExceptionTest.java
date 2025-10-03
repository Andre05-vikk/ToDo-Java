package ee.taltech.todo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationException.
 */
class ValidationExceptionTest {

    @Test
    void testConstructor_WithMessage() {
        String message = "Validation failed";
        ValidationException exception = new ValidationException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructor_WithMessageAndCause() {
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Invalid input");
        ValidationException exception = new ValidationException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testExceptionIsCheckedException() {
        assertTrue(Exception.class.isAssignableFrom(ValidationException.class));
    }

    @Test
    void testThrowAndCatch() {
        assertThrows(ValidationException.class, () -> {
            throw new ValidationException("Test validation error");
        });
    }
}
