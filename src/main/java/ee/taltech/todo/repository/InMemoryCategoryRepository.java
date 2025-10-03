package ee.taltech.todo.repository;

import ee.taltech.todo.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of CategoryRepository using ConcurrentHashMap.
 *
 * This implementation stores categories in memory and provides thread-safe operations.
 * Suitable for development, testing, and applications that don't require persistence.
 *
 * Design Pattern: Repository Pattern (implementation)
 * Thread Safety: Uses ConcurrentHashMap for thread-safe operations
 *
 * @author ToDo Application
 * @version 1.0
 */
public class InMemoryCategoryRepository implements CategoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCategoryRepository.class);

    /**
     * Thread-safe storage for categories.
     * Key: Category ID, Value: Category object
     */
    private final Map<String, Category> storage;

    /**
     * Default constructor initializing the storage.
     */
    public InMemoryCategoryRepository() {
        this.storage = new ConcurrentHashMap<>();
        logger.info("InMemoryCategoryRepository initialized");
    }

    @Override
    public Category save(Category category) {
        if (category == null) {
            logger.error("Attempted to save null category");
            throw new IllegalArgumentException("Category cannot be null");
        }

        if (category.getId() == null || category.getId().isEmpty()) {
            logger.error("Attempted to save category with null/empty ID");
            throw new IllegalArgumentException("Category ID cannot be null or empty");
        }

        boolean isUpdate = storage.containsKey(category.getId());
        storage.put(category.getId(), category);

        if (isUpdate) {
            logger.debug("Updated category: {}", category.getId());
        } else {
            logger.debug("Created new category: {}", category.getId());
        }

        return category;
    }

    @Override
    public Optional<Category> findById(String id) {
        if (id == null) {
            logger.error("Attempted to find category with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        Category category = storage.get(id);
        logger.debug("Find category by ID {}: {}", id, category != null ? "found" : "not found");
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>(storage.values());
        logger.debug("Retrieved all categories: {} found", categories.size());
        return categories;
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null) {
            logger.error("Attempted to delete category with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        Category removed = storage.remove(id);
        boolean deleted = removed != null;
        logger.debug("Delete category by ID {}: {}", id, deleted ? "success" : "not found");
        return deleted;
    }

    @Override
    public boolean delete(Category category) {
        if (category == null) {
            logger.error("Attempted to delete null category");
            throw new IllegalArgumentException("Category cannot be null");
        }

        return deleteById(category.getId());
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) {
            logger.error("Attempted to check existence with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }

        boolean exists = storage.containsKey(id);
        logger.debug("Check category exists by ID {}: {}", id, exists);
        return exists;
    }

    @Override
    public long count() {
        long count = storage.size();
        logger.debug("Total category count: {}", count);
        return count;
    }

    @Override
    public void deleteAll() {
        int previousCount = storage.size();
        storage.clear();
        logger.info("Deleted all categories (previous count: {})", previousCount);
    }

    @Override
    public Optional<Category> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("findByName called with null/empty name");
            return Optional.empty();
        }

        Optional<Category> result = storage.values().stream()
                .filter(category -> name.equals(category.getName()))
                .findFirst();

        logger.debug("Find category by name '{}': {}", name, result.isPresent() ? "found" : "not found");
        return result;
    }

    @Override
    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("existsByName called with null/empty name");
            return false;
        }

        boolean exists = storage.values().stream()
                .anyMatch(category -> name.equals(category.getName()));

        logger.debug("Check category exists by name '{}': {}", name, exists);
        return exists;
    }
}
