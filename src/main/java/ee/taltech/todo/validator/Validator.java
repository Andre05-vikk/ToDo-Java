package ee.taltech.todo.validator;

import ee.taltech.todo.exception.ValidationException;

import java.util.List;

/**
 * Generic validator interface for validating entities.
 *
 * Implements the Strategy Design Pattern - different validation strategies
 * can be implemented for different entity types.
 *
 * Design Pattern: Strategy Pattern
 *
 * @param <T> The type of entity to validate
 * @author ToDo Application
 * @version 1.0
 */
public interface Validator<T> {

    /**
     * Validates an entity and throws an exception if validation fails.
     *
     * @param entity The entity to validate
     * @throws ValidationException if validation fails
     */
    void validate(T entity) throws ValidationException;

    /**
     * Validates an entity and returns a list of validation errors.
     * Returns an empty list if validation succeeds.
     *
     * @param entity The entity to validate
     * @return List of validation error messages (empty if valid)
     */
    List<String> getValidationErrors(T entity);

    /**
     * Checks if an entity is valid.
     *
     * @param entity The entity to validate
     * @return true if entity is valid, false otherwise
     */
    default boolean isValid(T entity) {
        return getValidationErrors(entity).isEmpty();
    }
}
