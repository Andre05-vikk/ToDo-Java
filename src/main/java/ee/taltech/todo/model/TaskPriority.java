package ee.taltech.todo.model;

/**
 * Enumeration representing the priority levels of a task.
 *
 * Priority determines the urgency and importance of a task.
 * Tasks with higher priority should generally be completed first.
 *
 * @author ToDo Application
 * @version 1.0
 */
public enum TaskPriority {

    /**
     * Low priority task - can be done when time permits.
     */
    LOW(1, "Low", "Low priority task"),

    /**
     * Medium priority task - should be done in reasonable time.
     */
    MEDIUM(2, "Medium", "Medium priority task"),

    /**
     * High priority task - should be done soon.
     */
    HIGH(3, "High", "High priority task"),

    /**
     * Critical priority task - requires immediate attention.
     */
    CRITICAL(4, "Critical", "Critical priority - requires immediate attention");

    private final int level;
    private final String displayName;
    private final String description;

    /**
     * Constructor for TaskPriority enum.
     *
     * @param level       Numeric level for comparison (higher = more urgent)
     * @param displayName Human-readable name for display
     * @param description Description of this priority level
     */
    TaskPriority(int level, String displayName, String description) {
        this.level = level;
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Gets the numeric priority level.
     * Higher values indicate higher priority.
     *
     * @return The priority level (1-4)
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the display name of this priority.
     *
     * @return The human-readable priority name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the description of this priority.
     *
     * @return The priority description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Parses a string to a TaskPriority enum value.
     * Case-insensitive.
     *
     * @param value The string to parse
     * @return The corresponding TaskPriority
     * @throws IllegalArgumentException if the value doesn't match any priority
     */
    public static TaskPriority fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Priority value cannot be null");
        }

        for (TaskPriority priority : TaskPriority.values()) {
            if (priority.name().equalsIgnoreCase(value) ||
                priority.displayName.equalsIgnoreCase(value)) {
                return priority;
            }
        }

        throw new IllegalArgumentException("Invalid task priority: " + value);
    }

    /**
     * Checks if this priority is higher than another priority.
     *
     * @param other The priority to compare with
     * @return true if this priority is higher
     */
    public boolean isHigherThan(TaskPriority other) {
        return this.level > other.level;
    }

    /**
     * Checks if this priority is lower than another priority.
     *
     * @param other The priority to compare with
     * @return true if this priority is lower
     */
    public boolean isLowerThan(TaskPriority other) {
        return this.level < other.level;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
