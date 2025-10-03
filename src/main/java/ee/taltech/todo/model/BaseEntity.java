package ee.taltech.todo.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Abstract base entity class providing common fields for all domain entities.
 * Implements inheritance - all entities will extend this class.
 *
 * This demonstrates OOP principle: Inheritance (Pärilikkus)
 *
 * @author ToDo Application
 * @version 1.0
 */
public abstract class BaseEntity {

    /**
     * Unique identifier for the entity.
     */
    private String id;

    /**
     * Timestamp when the entity was created.
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Default constructor that generates a unique ID and sets creation timestamp.
     */
    protected BaseEntity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Constructor with ID parameter (useful for testing or reconstruction from storage).
     *
     * @param id The unique identifier for this entity
     */
    protected BaseEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters (Encapsulation - Kapseldamine)

    /**
     * Gets the unique identifier of this entity.
     *
     * @return The entity's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return When this entity was created
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets the last update timestamp.
     *
     * @return When this entity was last updated
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Updates the modification timestamp to current time.
     * Should be called whenever the entity is modified.
     */
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the ID (useful for reconstruction from database).
     *
     * @param id The ID to set
     */
    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the creation timestamp (useful for reconstruction from database).
     *
     * @param createdAt The creation timestamp
     */
    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets the update timestamp (useful for reconstruction from database).
     *
     * @param updatedAt The update timestamp
     */
    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
