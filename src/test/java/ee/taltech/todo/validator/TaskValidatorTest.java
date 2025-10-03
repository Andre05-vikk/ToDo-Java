package ee.taltech.todo.validator;

import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskValidator.
 */
class TaskValidatorTest {

    private TaskValidator validator;
    private Task task;

    @BeforeEach
    void setUp() {
        validator = new TaskValidator();
        task = new Task();
        task.setTitle("Valid Task");
        task.setDescription("Valid description");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.MEDIUM);
    }

    @Test
    void testValidate_WithValidTask_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validator.validate(task));
    }

    @Test
    void testValidate_WithNullTask_ShouldThrowException() {
        assertThrows(ValidationException.class, () -> validator.validate(null));
    }

    @Test
    void testValidate_WithNullTitle_ShouldThrowException() {
        task.setTitle(null);
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testValidate_WithEmptyTitle_ShouldThrowException() {
        task.setTitle("");
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testValidate_WithWhitespaceTitle_ShouldThrowException() {
        task.setTitle("   ");
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testValidate_WithTooLongTitle_ShouldThrowException() {
        String longTitle = "A".repeat(201);
        task.setTitle(longTitle);
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testValidate_WithMaxLengthTitle_ShouldNotThrowException() {
        String maxTitle = "A".repeat(200);
        task.setTitle(maxTitle);
        assertDoesNotThrow(() -> validator.validate(task));
    }

    @Test
    void testValidate_WithTooLongDescription_ShouldThrowException() {
        String longDescription = "A".repeat(1001);
        task.setDescription(longDescription);
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testValidate_WithMaxLengthDescription_ShouldNotThrowException() {
        String maxDescription = "A".repeat(1000);
        task.setDescription(maxDescription);
        assertDoesNotThrow(() -> validator.validate(task));
    }

    @Test
    void testValidate_WithNullDescription_ShouldNotThrowException() {
        task.setDescription(null);
        assertDoesNotThrow(() -> validator.validate(task));
    }

    @Test
    void testValidate_WithEmptyDescription_ShouldNotThrowException() {
        task.setDescription("");
        assertDoesNotThrow(() -> validator.validate(task));
    }

    @Test
    void testValidate_WithNullStatus_ShouldThrowException() {
        task.setStatus(null);
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testValidate_WithNullPriority_ShouldThrowException() {
        task.setPriority(null);
        assertThrows(ValidationException.class, () -> validator.validate(task));
    }

    @Test
    void testGetValidationErrors_WithValidTask_ShouldReturnEmptyList() {
        var errors = validator.getValidationErrors(task);
        assertTrue(errors.isEmpty());
    }

    @Test
    void testGetValidationErrors_WithMultipleErrors_ShouldReturnAllErrors() {
        task.setTitle(null);
        task.setStatus(null);
        task.setPriority(null);

        var errors = validator.getValidationErrors(task);

        assertFalse(errors.isEmpty(), "Should have errors");
        assertTrue(errors.size() >= 3, "Should have at least 3 errors, but got: " + errors.size());
    }

    @Test
    void testValidate_WithSpecialCharactersInTitle_ShouldNotThrowException() {
        task.setTitle("Test @ #$% Task!");
        assertDoesNotThrow(() -> validator.validate(task));
    }

    @Test
    void testValidate_WithUnicodeCharactersInTitle_ShouldNotThrowException() {
        task.setTitle("Ülesanne tähtajaga 🚀");
        assertDoesNotThrow(() -> validator.validate(task));
    }
}
