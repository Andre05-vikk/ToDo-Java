package ee.taltech.todo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Task entity.
 */
class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void testTaskCreation_ShouldGenerateIdAndTimestamps() {
        assertNotNull(task.getId(), "Task ID should be generated");
        assertNotNull(task.getCreatedAt(), "Created timestamp should be set");
        assertNotNull(task.getUpdatedAt(), "Updated timestamp should be set");
    }

    @Test
    void testTaskCreation_ShouldHaveDefaultValues() {
        assertEquals(TaskStatus.PENDING, task.getStatus(), "Default status should be PENDING");
        assertEquals(TaskPriority.MEDIUM, task.getPriority(), "Default priority should be MEDIUM");
        assertFalse(task.isStarred(), "Default starred should be false");
    }

    @Test
    void testSetTitle_ShouldUpdateTitle() {
        String title = "Test Task";
        task.setTitle(title);
        assertEquals(title, task.getTitle());
    }

    @Test
    void testSetDescription_ShouldUpdateDescription() {
        String description = "This is a test task description";
        task.setDescription(description);
        assertEquals(description, task.getDescription());
    }

    @Test
    void testSetStatus_ShouldUpdateStatus() {
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void testSetPriority_ShouldUpdatePriority() {
        task.setPriority(TaskPriority.HIGH);
        assertEquals(TaskPriority.HIGH, task.getPriority());
    }

    @Test
    void testSetDueDate_ShouldUpdateDueDate() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);
        task.setDueDate(dueDate);
        assertEquals(dueDate, task.getDueDate());
    }

    @Test
    void testSetCategory_ShouldUpdateCategory() {
        Category category = new Category();
        category.setName("Work");
        task.setCategory(category);
        assertEquals(category, task.getCategory());
    }

    @Test
    void testSetStarred_ShouldUpdateStarredStatus() {
        task.setStarred(true);
        assertTrue(task.isStarred());

        task.setStarred(false);
        assertFalse(task.isStarred());
    }

    @Test
    void testComplete_ShouldChangeStatusToCompleted() {
        task.complete();
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void testComplete_ShouldUpdateTimestamp() {
        LocalDateTime beforeComplete = task.getUpdatedAt();

        // Wait a tiny bit to ensure timestamp changes
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        task.complete();
        LocalDateTime afterComplete = task.getUpdatedAt();

        assertTrue(afterComplete.isAfter(beforeComplete),
                   "Updated timestamp should be after completion");
    }

    @Test
    void testStart_ShouldChangeStatusToInProgress() {
        task.start();
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void testStart_ShouldUpdateTimestamp() {
        LocalDateTime beforeStart = task.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        task.start();
        LocalDateTime afterStart = task.getUpdatedAt();

        assertTrue(afterStart.isAfter(beforeStart),
                   "Updated timestamp should be after starting");
    }

    @Test
    void testCancel_ShouldChangeStatusToCancelled() {
        task.cancel();
        assertEquals(TaskStatus.CANCELLED, task.getStatus());
    }

    @Test
    void testIsOverdue_WhenNoDueDate_ShouldReturnFalse() {
        assertFalse(task.isOverdue(), "Task without due date should not be overdue");
    }

    @Test
    void testIsOverdue_WhenDueDateInFuture_ShouldReturnFalse() {
        task.setDueDate(LocalDateTime.now().plusDays(1));
        assertFalse(task.isOverdue(), "Task with future due date should not be overdue");
    }

    @Test
    void testIsOverdue_WhenDueDateInPast_ShouldReturnTrue() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        assertTrue(task.isOverdue(), "Task with past due date should be overdue");
    }

    @Test
    void testIsOverdue_WhenCompleted_ShouldReturnFalse() {
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.complete();
        assertFalse(task.isOverdue(), "Completed task should not be overdue");
    }

    @Test
    void testUpdateTimestamp_ShouldUpdateUpdatedAt() {
        LocalDateTime before = task.getUpdatedAt();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        task.setTitle("Updated Title");
        LocalDateTime after = task.getUpdatedAt();

        assertTrue(after.isAfter(before), "Updated timestamp should change");
    }

    @Test
    void testTaskWithAllFields_ShouldStoreAllData() {
        String title = "Complete Project";
        String description = "Finish the ToDo application";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        TaskPriority priority = TaskPriority.HIGH;
        LocalDateTime dueDate = LocalDateTime.now().plusWeeks(1);
        Category category = new Category();
        category.setName("Work");
        boolean starred = true;

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setCategory(category);
        task.setStarred(starred);

        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
        assertEquals(priority, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(category, task.getCategory());
        assertEquals(starred, task.isStarred());
    }

    @Test
    void testMultipleTaskInstances_ShouldHaveUniqueIds() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();

        assertNotEquals(task1.getId(), task2.getId(), "Tasks should have unique IDs");
        assertNotEquals(task1.getId(), task3.getId(), "Tasks should have unique IDs");
        assertNotEquals(task2.getId(), task3.getId(), "Tasks should have unique IDs");
    }
}
