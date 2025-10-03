package ee.taltech.todo.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskNotFoundException.
 */
class TaskNotFoundExceptionTest {

    @Test
    void testConstructor_WithMessage() {
        String message = "Task not found";
        TaskNotFoundException exception = new TaskNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testConstructor_WithMessageAndCause() {
        String message = "Task not found";
        Throwable cause = new RuntimeException("Root cause");
        TaskNotFoundException exception = new TaskNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testForId_ShouldCreateExceptionWithFormattedMessage() {
        String taskId = "task-456";
        TaskNotFoundException exception = TaskNotFoundException.forId(taskId);

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains(taskId));
        assertTrue(exception.getMessage().contains("Task not found with ID"));
    }

    @Test
    void testExceptionIsCheckedException() {
        assertTrue(Exception.class.isAssignableFrom(TaskNotFoundException.class));
    }

    @Test
    void testThrowAndCatch() {
        assertThrows(TaskNotFoundException.class, () -> {
            throw new TaskNotFoundException("Test exception");
        });
    }
}
