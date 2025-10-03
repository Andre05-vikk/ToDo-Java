package ee.taltech.todo.validator;

import ee.taltech.todo.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base validator implementing common validation logic.
 *
 * Implements the Template Method Design Pattern - defines the skeleton
 * of validation algorithm, allowing subclasses to override specific steps.
 *
 * Design Patterns:
 * - Template Method Pattern: validate() method defines the algorithm
 * - Strategy Pattern: Different validators implement specific validation
 *
 * @param <T> The type of entity to validate
 * @author ToDo Application
 * @version 1.0
 */
public abstract class BaseValidator<T> implements Validator<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseValidator.class);

    /**
     * Template method for validation.
     * Validates the entity and throws an exception if validation fails.
     *
     * @param entity The entity to validate
     * @throws ValidationException if validation fails
     */
    @Override
    public void validate(T entity) throws ValidationException {
        logger.debug("Validating entity: {}", entity != null ? entity.getClass().getSimpleName() : "null");

        List<String> errors = getValidationErrors(entity);

        if (!errors.isEmpty()) {
            String errorMessage = String.join("; ", errors);
            logger.error("Validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }

        logger.debug("Validation successful");
    }

    /**
     * Gets all validation errors for an entity.
     * Calls all specific validation methods and collects errors.
     *
     * @param entity The entity to validate
     * @return List of validation error messages
     */
    @Override
    public List<String> getValidationErrors(T entity) {
        List<String> errors = new ArrayList<>();

        // Basic null check
        if (entity == null) {
            errors.add("Entity cannot be null");
            return errors;
        }

        // Call specific validation methods (to be implemented by subclasses)
        validateRequired(entity, errors);
        validateFormat(entity, errors);
        validateLength(entity, errors);
        validateBusinessRules(entity, errors);

        return errors;
    }

    /**
     * Validates required fields.
     * Subclasses should override this to implement specific required field validation.
     *
     * @param entity The entity to validate
     * @param errors List to add errors to
     */
    protected abstract void validateRequired(T entity, List<String> errors);

    /**
     * Validates field formats (e.g., email format, color format).
     * Subclasses can override this to implement specific format validation.
     *
     * @param entity The entity to validate
     * @param errors List to add errors to
     */
    protected void validateFormat(T entity, List<String> errors) {
        // Default implementation does nothing
        // Subclasses can override if needed
    }

    /**
     * Validates field lengths.
     * Subclasses can override this to implement specific length validation.
     *
     * @param entity The entity to validate
     * @param errors List to add errors to
     */
    protected void validateLength(T entity, List<String> errors) {
        // Default implementation does nothing
        // Subclasses can override if needed
    }

    /**
     * Validates business rules.
     * Subclasses can override this to implement specific business rule validation.
     *
     * @param entity The entity to validate
     * @param errors List to add errors to
     */
    protected void validateBusinessRules(T entity, List<String> errors) {
        // Default implementation does nothing
        // Subclasses can override if needed
    }

    /**
     * Helper method to check if a string is null or empty.
     *
     * @param value The string to check
     * @return true if string is null or empty
     */
    protected boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Helper method to check if a string exceeds maximum length.
     *
     * @param value     The string to check
     * @param maxLength The maximum allowed length
     * @return true if string exceeds max length
     */
    protected boolean exceedsMaxLength(String value, int maxLength) {
        return value != null && value.length() > maxLength;
    }
}
