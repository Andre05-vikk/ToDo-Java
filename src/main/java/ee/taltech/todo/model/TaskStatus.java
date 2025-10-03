package ee.taltech.todo.model;

/**
 * Enumeration representing the possible states of a task.
 *
 * Using enums provides type safety and prevents invalid status values.
 * This is a Java best practice for representing fixed sets of constants.
 *
 * @author ToDo Application
 * @version 1.0
 */
public enum TaskStatus {

    /**
     * Task is created but not yet started.
     */
    PENDING("Pending", "Task is waiting to be started"),

    /**
     * Task is currently being worked on.
     */
    IN_PROGRESS("In Progress", "Task is currently being worked on"),

    /**
     * Task has been completed successfully.
     */
    COMPLETED("Completed", "Task has been completed"),

    /**
     * Task has been cancelled and will not be completed.
     */
    CANCELLED("Cancelled", "Task has been cancelled");

    private final String displayName;
    private final String description;

    /**
     * Constructor for TaskStatus enum.
     *
     * @param displayName Human-readable name for display
     * @param description Description of what this status means
     */
    TaskStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Gets the display name of this status.
     *
     * @return The human-readable status name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the description of this status.
     *
     * @return The status description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parses a string to a TaskStatus enum value.
     * Case-insensitive.
     *
     * @param value The string to parse
     * @return The corresponding TaskStatus
     * @throws IllegalArgumentException if the value doesn't match any status
     */
    public static TaskStatus fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Status value cannot be null");
        }

        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(value) ||
                status.displayName.equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid task status: " + value);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
