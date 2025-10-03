package ee.taltech.todo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DuplicateEntityException.
 */
class DuplicateEntityExceptionTest {

    @Test
    void testConstructor_WithMessage() {
        String message = "Duplicate entity found";
        DuplicateEntityException exception = new DuplicateEntityException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructor_WithMessageAndCause() {
        String message = "Duplicate entity found";
        Throwable cause = new RuntimeException("Root cause");
        DuplicateEntityException exception = new DuplicateEntityException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testForEntity_ShouldCreateExceptionWithFormattedMessage() {
        String entityType = "Category";
        String identifier = "Work";
        DuplicateEntityException exception = DuplicateEntityException.forEntity(entityType, identifier);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(entityType));
        assertTrue(exception.getMessage().contains(identifier));
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    void testExceptionIsCheckedException() {
        assertTrue(Exception.class.isAssignableFrom(DuplicateEntityException.class));
    }

    @Test
    void testThrowAndCatch() {
        assertThrows(DuplicateEntityException.class, () -> {
            throw new DuplicateEntityException("Test exception");
        });
    }
}
