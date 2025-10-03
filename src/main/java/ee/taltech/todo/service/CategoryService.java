package ee.taltech.todo.service;

import ee.taltech.todo.exception.CategoryNotFoundException;
import ee.taltech.todo.exception.DuplicateEntityException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;

import java.util.List;

/**
 * Service interface for category business logic operations.
 *
 * This interface defines the business operations for managing categories.
 *
 * @author ToDo Application
 * @version 1.0
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param category The category to create
     * @return The created category
     * @throws ValidationException      if category validation fails
     * @throws DuplicateEntityException if category name already exists
     */
    Category createCategory(Category category) throws ValidationException, DuplicateEntityException;

    /**
     * Updates an existing category.
     *
     * @param category The category with updated information
     * @return The updated category
     * @throws CategoryNotFoundException if category doesn't exist
     * @throws ValidationException       if category validation fails
     * @throws DuplicateEntityException  if new name conflicts with existing category
     */
    Category updateCategory(Category category)
            throws CategoryNotFoundException, ValidationException, DuplicateEntityException;

    /**
     * Finds a category by its ID.
     *
     * @param id The category ID
     * @return The category
     * @throws CategoryNotFoundException if category doesn't exist
     */
    Category getCategoryById(String id) throws CategoryNotFoundException;

    /**
     * Finds a category by its name.
     *
     * @param name The category name
     * @return The category
     * @throws CategoryNotFoundException if category doesn't exist
     */
    Category getCategoryByName(String name) throws CategoryNotFoundException;

    /**
     * Retrieves all categories.
     *
     * @return List of all categories
     */
    List<Category> getAllCategories();

    /**
     * Deletes a category by its ID.
     *
     * @param id The category ID
     * @throws CategoryNotFoundException if category doesn't exist
     */
    void deleteCategory(String id) throws CategoryNotFoundException;

    /**
     * Checks if a category with the given name exists.
     *
     * @param name The category name
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Gets the total count of categories.
     *
     * @return Total number of categories
     */
    long getTotalCount();
}
