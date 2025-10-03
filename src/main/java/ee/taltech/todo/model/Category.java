package ee.taltech.todo.model;

import java.util.Objects;

/**
 * Category entity for organizing tasks into logical groups.
 *
 * Demonstrates OOP inheritance - extends BaseEntity to inherit common fields.
 * Categories can be used to group related tasks (e.g., Work, Personal, Shopping).
 *
 * @author ToDo Application
 * @version 1.0
 */
public class Category extends BaseEntity {

    /**
     * The name of the category (e.g., "Work", "Personal", "Shopping").
     */
    private String name;

    /**
     * Optional description providing more details about this category.
     */
    private String description;

    /**
     * Optional color code for visual representation (hex format: #RRGGBB).
     */
    private String color;

    /**
     * Default constructor.
     */
    public Category() {
        super();
    }

    /**
     * Constructor with name parameter.
     *
     * @param name The category name
     */
    public Category(String name) {
        super();
        this.name = name;
    }

    /**
     * Full constructor with all fields.
     *
     * @param name        The category name
     * @param description The category description
     * @param color       The category color (hex format)
     */
    public Category(String name, String description, String color) {
        super();
        this.name = name;
        this.description = description;
        this.color = color;
    }

    // Getters and Setters

    /**
     * Gets the category name.
     *
     * @return The name of this category
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the category name.
     *
     * @param name The new category name
     */
    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }

    /**
     * Gets the category description.
     *
     * @return The category description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the category description.
     *
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
        updateTimestamp();
    }

    /**
     * Gets the category color.
     *
     * @return The color in hex format
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the category color.
     *
     * @param color The color in hex format (#RRGGBB)
     */
    public void setColor(String color) {
        this.color = color;
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
