package ee.taltech.todo.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface defining CRUD operations for all entities.
 *
 * Implements the Repository Design Pattern - provides abstraction for data access.
 * This allows switching between different storage implementations (in-memory, database, file)
 * without changing the business logic.
 *
 * Design Pattern: Repository Pattern
 *
 * @param <T> The entity type this repository manages
 * @author ToDo Application
 * @version 1.0
 */
public interface Repository<T> {

    /**
     * Saves an entity to the repository.
     * If entity already exists (by ID), it will be updated.
     *
     * @param entity The entity to save
     * @return The saved entity
     * @throws IllegalArgumentException if entity is null
     */
    T save(T entity);

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id The entity ID
     * @return Optional containing the entity if found, empty otherwise
     * @throws IllegalArgumentException if id is null
     */
    Optional<T> findById(String id);

    /**
     * Retrieves all entities from the repository.
     *
     * @return List of all entities (empty list if none exist)
     */
    List<T> findAll();

    /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete
     * @return true if entity was deleted, false if not found
     * @throws IllegalArgumentException if id is null
     */
    boolean deleteById(String id);

    /**
     * Deletes an entity from the repository.
     *
     * @param entity The entity to delete
     * @return true if entity was deleted, false if not found
     * @throws IllegalArgumentException if entity is null
     */
    boolean delete(T entity);

    /**
     * Checks if an entity with given ID exists.
     *
     * @param id The entity ID
     * @return true if entity exists, false otherwise
     * @throws IllegalArgumentException if id is null
     */
    boolean existsById(String id);

    /**
     * Returns the total count of entities in the repository.
     *
     * @return The number of entities
     */
    long count();

    /**
     * Deletes all entities from the repository.
     */
    void deleteAll();
}
