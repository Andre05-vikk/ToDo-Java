package ee.taltech.todo.dto;

import ee.taltech.todo.model.Category;

/**
 * Request DTO for creating a new category.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class CreateCategoryRequest {

    private String name;
    private String description;
    private String color;

    /**
     * Default constructor.
     */
    public CreateCategoryRequest() {
    }

    /**
     * Converts this request to a Category entity.
     *
     * @return New Category entity
     */
    public Category toEntity() {
        Category category = new Category();
        category.setName(this.name);
        category.setDescription(this.description);
        category.setColor(this.color);
        return category;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
