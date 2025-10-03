package ee.taltech.todo.repository;

import ee.taltech.todo.model.Category;

import java.util.Optional;

/**
 * Repository interface for Category entity operations.
 *
 * Extends the generic Repository interface and adds category-specific query methods.
 *
 * @author ToDo Application
 * @version 1.0
 */
public interface CategoryRepository extends Repository<Category> {

    /**
     * Finds a category by its name.
     * Category names should be unique.
     *
     * @param name The category name (case-sensitive)
     * @return Optional containing the category if found, empty otherwise
     */
    Optional<Category> findByName(String name);

    /**
     * Checks if a category with the given name exists.
     *
     * @param name The category name
     * @return true if a category with this name exists, false otherwise
     */
    boolean existsByName(String name);
}
