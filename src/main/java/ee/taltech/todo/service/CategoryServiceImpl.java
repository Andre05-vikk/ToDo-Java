package ee.taltech.todo.service;

import ee.taltech.todo.exception.CategoryNotFoundException;
import ee.taltech.todo.exception.DuplicateEntityException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;
import ee.taltech.todo.repository.CategoryRepository;
import ee.taltech.todo.validator.CategoryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of CategoryService interface.
 *
 * Demonstrates OOP Composition: CategoryService HAS-A CategoryRepository.
 * This service layer contains all business logic for category management.
 *
 * Design Patterns:
 * - Composition: Service has-a Repository
 * - Strategy Pattern: Uses CategoryValidator for validation
 *
 * @author ToDo Application
 * @version 1.0
 */
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    /**
     * Composition: CategoryService HAS-A CategoryRepository.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Strategy Pattern: CategoryValidator for validation logic.
     */
    private final CategoryValidator categoryValidator;

    /**
     * Constructor with dependency injection.
     *
     * @param categoryRepository The category repository
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository,
                "CategoryRepository cannot be null");
        this.categoryValidator = new CategoryValidator();
        logger.info("CategoryServiceImpl initialized");
    }

    @Override
    public Category createCategory(Category category) throws ValidationException, DuplicateEntityException {
        logger.debug("Creating new category: {}", category != null ? category.getName() : "null");

        validateCategory(category);

        // Check for duplicate name
        if (categoryRepository.existsByName(category.getName())) {
            logger.warn("Attempted to create category with duplicate name: {}", category.getName());
            throw DuplicateEntityException.forEntity("Category", category.getName());
        }

        Category savedCategory = categoryRepository.save(category);
        logger.info("Category created successfully: ID={}, Name={}", savedCategory.getId(), savedCategory.getName());

        return savedCategory;
    }

    @Override
    public Category updateCategory(Category category)
            throws CategoryNotFoundException, ValidationException, DuplicateEntityException {
        logger.debug("Updating category: {}", category != null ? category.getId() : "null");

        validateCategory(category);

        // Check if category exists
        if (!categoryRepository.existsById(category.getId())) {
            logger.warn("Attempted to update non-existent category: {}", category.getId());
            throw CategoryNotFoundException.forId(category.getId());
        }

        // Check for duplicate name (excluding current category)
        categoryRepository.findByName(category.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(category.getId())) {
                logger.warn("Attempted to update category to duplicate name: {}", category.getName());
                throw new RuntimeException(
                        new DuplicateEntityException("Category name already exists: " + category.getName())
                );
            }
        });

        Category updatedCategory = categoryRepository.save(category);
        logger.info("Category updated successfully: ID={}, Name={}",
                updatedCategory.getId(), updatedCategory.getName());

        return updatedCategory;
    }

    @Override
    public Category getCategoryById(String id) throws CategoryNotFoundException {
        logger.debug("Fetching category by ID: {}", id);

        if (id == null || id.trim().isEmpty()) {
            logger.error("getCategoryById called with null or empty ID");
            throw new IllegalArgumentException("Category ID cannot be null or empty");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category not found: {}", id);
                    return CategoryNotFoundException.forId(id);
                });
    }

    @Override
    public Category getCategoryByName(String name) throws CategoryNotFoundException {
        logger.debug("Fetching category by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            logger.error("getCategoryByName called with null or empty name");
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        return categoryRepository.findByName(name)
                .orElseThrow(() -> {
                    logger.warn("Category not found with name: {}", name);
                    return CategoryNotFoundException.forName(name);
                });
    }

    @Override
    public List<Category> getAllCategories() {
        logger.debug("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        logger.debug("Retrieved {} categories", categories.size());
        return categories;
    }

    @Override
    public void deleteCategory(String id) throws CategoryNotFoundException {
        logger.debug("Deleting category: {}", id);

        if (id == null || id.trim().isEmpty()) {
            logger.error("deleteCategory called with null or empty ID");
            throw new IllegalArgumentException("Category ID cannot be null or empty");
        }

        // Check if category exists before deleting
        if (!categoryRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent category: {}", id);
            throw CategoryNotFoundException.forId(id);
        }

        categoryRepository.deleteById(id);
        logger.info("Category deleted successfully: {}", id);
    }

    @Override
    public boolean existsByName(String name) {
        logger.debug("Checking if category exists by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            logger.warn("existsByName called with null or empty name");
            return false;
        }

        boolean exists = categoryRepository.existsByName(name);
        logger.debug("Category exists with name '{}': {}", name, exists);
        return exists;
    }

    @Override
    public long getTotalCount() {
        logger.debug("Getting total category count");
        long count = categoryRepository.count();
        logger.debug("Total category count: {}", count);
        return count;
    }

    /**
     * Validates a category before saving or updating.
     * Uses CategoryValidator (Strategy Pattern) for validation.
     *
     * @param category The category to validate
     * @throws ValidationException if validation fails
     */
    private void validateCategory(Category category) throws ValidationException {
        categoryValidator.validate(category);
    }
}
