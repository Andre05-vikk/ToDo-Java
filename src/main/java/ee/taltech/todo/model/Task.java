package ee.taltech.todo.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Task entity representing a todo item in the application.
 *
 * Demonstrates multiple OOP principles:
 * - Inheritance: extends BaseEntity
 * - Composition: has-a Category (relationship)
 * - Encapsulation: private fields with public getters/setters
 *
 * @author ToDo Application
 * @version 1.0
 */
public class Task extends BaseEntity {

    /**
     * The title/name of the task.
     */
    private String title;

    /**
     * Detailed description of what needs to be done.
     */
    private String description;

    /**
     * Current status of the task.
     */
    private TaskStatus status;

    /**
     * Priority level of the task.
     */
    private TaskPriority priority;

    /**
     * Optional due date for task completion.
     */
    private LocalDateTime dueDate;

    /**
     * Optional category for organizing tasks.
     * Demonstrates Composition - Task HAS-A Category.
     */
    private Category category;

    /**
     * Flag indicating if the task is marked as favorite/starred.
     */
    private boolean starred;

    /**
     * Default constructor.
     * Creates a task with PENDING status and MEDIUM priority.
     */
    public Task() {
        super();
        this.status = TaskStatus.PENDING;
        this.priority = TaskPriority.MEDIUM;
        this.starred = false;
    }

    /**
     * Constructor with title.
     *
     * @param title The task title
     */
    public Task(String title) {
        this();
        this.title = title;
    }

    /**
     * Constructor with title and description.
     *
     * @param title       The task title
     * @param description The task description
     */
    public Task(String title, String description) {
        this(title);
        this.description = description;
    }

    /**
     * Full constructor with all main fields.
     *
     * @param title       The task title
     * @param description The task description
     * @param priority    The task priority
     * @param category    The task category
     */
    public Task(String title, String description, TaskPriority priority, Category category) {
        this(title, description);
        this.priority = priority;
        this.category = category;
    }

    // Business Logic Methods

    /**
     * Marks the task as completed.
     * Changes status to COMPLETED and updates timestamp.
     */
    public void complete() {
        this.status = TaskStatus.COMPLETED;
        updateTimestamp();
    }

    /**
     * Starts working on the task.
     * Changes status to IN_PROGRESS and updates timestamp.
     */
    public void start() {
        this.status = TaskStatus.IN_PROGRESS;
        updateTimestamp();
    }

    /**
     * Cancels the task.
     * Changes status to CANCELLED and updates timestamp.
     */
    public void cancel() {
        this.status = TaskStatus.CANCELLED;
        updateTimestamp();
    }

    /**
     * Checks if the task is overdue.
     *
     * @return true if task has a due date and it's in the past
     */
    public boolean isOverdue() {
        if (dueDate == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate) &&
               status != TaskStatus.COMPLETED &&
               status != TaskStatus.CANCELLED;
    }

    /**
     * Checks if the task is completed.
     *
     * @return true if status is COMPLETED
     */
    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    /**
     * Toggles the starred/favorite status.
     */
    public void toggleStarred() {
        this.starred = !this.starred;
        updateTimestamp();
    }

    // Getters and Setters

    /**
     * Gets the task title.
     *
     * @return The task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the task title.
     *
     * @param title The new title
     */
    public void setTitle(String title) {
        this.title = title;
        updateTimestamp();
    }

    /**
     * Gets the task description.
     *
     * @return The task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the task description.
     *
     * @param description The new description
     */
    public void setDescription(String description) {
        this.description = description;
        updateTimestamp();
    }

    /**
     * Gets the task status.
     *
     * @return The current status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the task status.
     *
     * @param status The new status
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
        updateTimestamp();
    }

    /**
     * Gets the task priority.
     *
     * @return The task priority
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Sets the task priority.
     *
     * @param priority The new priority
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
        updateTimestamp();
    }

    /**
     * Gets the due date.
     *
     * @return The due date, or null if not set
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate The new due date
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        updateTimestamp();
    }

    /**
     * Gets the task category.
     *
     * @return The category, or null if not set
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the task category.
     *
     * @param category The new category
     */
    public void setCategory(Category category) {
        this.category = category;
        updateTimestamp();
    }

    /**
     * Checks if task is starred.
     *
     * @return true if starred
     */
    public boolean isStarred() {
        return starred;
    }

    /**
     * Sets the starred status.
     *
     * @param starred The new starred status
     */
    public void setStarred(boolean starred) {
        this.starred = starred;
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) &&
               Objects.equals(getCreatedAt(), task.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, getCreatedAt());
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + getId() + '\'' +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", category=" + (category != null ? category.getName() : "none") +
                ", starred=" + starred +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
