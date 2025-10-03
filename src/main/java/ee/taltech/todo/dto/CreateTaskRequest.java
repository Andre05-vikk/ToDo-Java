package ee.taltech.todo.dto;

import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;

import java.time.LocalDateTime;

/**
 * Request DTO for creating a new task.
 *
 * Contains only the fields needed to create a task.
 * Separates API request structure from domain model.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class CreateTaskRequest {

    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private String categoryId;

    /**
     * Default constructor.
     */
    public CreateTaskRequest() {
    }

    /**
     * Converts this request to a Task entity.
     *
     * @return New Task entity
     */
    public Task toEntity() {
        Task task = new Task();
        task.setTitle(this.title);
        task.setDescription(this.description);

        if (this.priority != null) {
            task.setPriority(this.priority);
        }

        task.setDueDate(this.dueDate);

        return task;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
