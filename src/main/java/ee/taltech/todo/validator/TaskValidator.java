package ee.taltech.todo.validator;

import ee.taltech.todo.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Validator for Task entities.
 *
 * Implements specific validation rules for tasks using the Strategy pattern.
 * Extends BaseValidator to utilize the Template Method pattern.
 *
 * Design Patterns:
 * - Strategy Pattern: Concrete validation strategy for Task
 * - Template Method Pattern: Uses BaseValidator's template methods
 *
 * @author ToDo Application
 * @version 1.0
 */
public class TaskValidator extends BaseValidator<Task> {

    private static final Logger logger = LoggerFactory.getLogger(TaskValidator.class);

    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_DESCRIPTION_LENGTH = 1000;

    @Override
    protected void validateRequired(Task task, List<String> errors) {
        logger.debug("Validating required fields for task");

        // Title is required
        if (isNullOrEmpty(task.getTitle())) {
            errors.add("Task title is required and cannot be empty");
        }

        // Status is required (should always be set by constructor, but check anyway)
        if (task.getStatus() == null) {
            errors.add("Task status cannot be null");
        }

        // Priority is required (should always be set by constructor, but check anyway)
        if (task.getPriority() == null) {
            errors.add("Task priority cannot be null");
        }
    }

    @Override
    protected void validateLength(Task task, List<String> errors) {
        logger.debug("Validating field lengths for task");

        // Validate title length
        if (exceedsMaxLength(task.getTitle(), MAX_TITLE_LENGTH)) {
            errors.add(String.format("Task title cannot exceed %d characters (current: %d)",
                    MAX_TITLE_LENGTH, task.getTitle().length()));
        }

        // Validate description length if present
        if (exceedsMaxLength(task.getDescription(), MAX_DESCRIPTION_LENGTH)) {
            errors.add(String.format("Task description cannot exceed %d characters (current: %d)",
                    MAX_DESCRIPTION_LENGTH, task.getDescription().length()));
        }
    }

    @Override
    protected void validateBusinessRules(Task task, List<String> errors) {
        logger.debug("Validating business rules for task");

        // Check if due date is in the past (warning, not error)
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            logger.warn("Task has due date in the past: {}", task.getDueDate());
            // Note: We don't add this as an error, just log it as a warning
            // Business may decide to allow tasks with past due dates
        }

        // Validate that completed tasks should not have future due dates
        // (optional business rule - can be adjusted based on requirements)
        if (task.isCompleted() && task.getDueDate() != null &&
            task.getDueDate().isAfter(LocalDateTime.now())) {
            logger.debug("Completed task has future due date - this is acceptable");
            // This is acceptable, just noting it
        }
    }

    /**
     * Additional validation method specific to tasks.
     * Validates task title format (no special restrictions currently).
     *
     * @param task The task to validate
     * @return true if title format is valid
     */
    public boolean isValidTitle(String title) {
        if (isNullOrEmpty(title)) {
            return false;
        }

        if (exceedsMaxLength(title, MAX_TITLE_LENGTH)) {
            return false;
        }

        // Could add more format rules here, e.g.:
        // - No special characters
        // - Minimum length
        // - etc.

        return true;
    }

    /**
     * Validates that a due date is valid for a task.
     *
     * @param dueDate The due date to validate
     * @return true if due date is valid (can be null or future date)
     */
    public boolean isValidDueDate(LocalDateTime dueDate) {
        // Null due date is valid (optional field)
        if (dueDate == null) {
            return true;
        }

        // Due dates can be in the past (for already created tasks)
        // This is a business decision - adjust as needed
        return true;
    }
}
