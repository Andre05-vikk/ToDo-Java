package ee.taltech.todo.dto;

import ee.taltech.todo.model.Category;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaskDTO.
 */
class TaskDTOTest {

    @Test
    void testFromEntity_WithCompleteTask_ShouldMapAllFields() {
        // Arrange
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority(TaskPriority.HIGH);
        task.setStarred(true);
        task.setDueDate(LocalDateTime.of(2025, 12, 31, 23, 59));

        Category category = new Category();
        category.setName("Work");
        task.setCategory(category);

        // Act
        TaskDTO dto = TaskDTO.fromEntity(task);

        // Assert
        assertNotNull(dto);
        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getTitle(), dto.getTitle());
        assertEquals(task.getDescription(), dto.getDescription());
        assertEquals(task.getStatus(), dto.getStatus());
        assertEquals(task.getPriority(), dto.getPriority());
        assertEquals(task.isStarred(), dto.isStarred());
        assertEquals(task.getDueDate(), dto.getDueDate());
        assertEquals("Work", dto.getCategoryName());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void testFromEntity_WithMinimalTask_ShouldMapRequiredFields() {
        Task task = new Task();
        task.setTitle("Minimal Task");

        TaskDTO dto = TaskDTO.fromEntity(task);

        assertNotNull(dto);
        assertEquals(task.getId(), dto.getId());
        assertEquals("Minimal Task", dto.getTitle());
        assertNull(dto.getDescription());
        assertEquals(TaskStatus.PENDING, dto.getStatus());
        assertEquals(TaskPriority.MEDIUM, dto.getPriority());
        assertFalse(dto.isStarred());
        assertNull(dto.getDueDate());
        assertNull(dto.getCategoryName());
    }

    @Test
    void testFromEntity_WithNullCategory_ShouldMapWithoutCategory() {
        Task task = new Task();
        task.setTitle("Task Without Category");
        task.setCategory(null);

        TaskDTO dto = TaskDTO.fromEntity(task);

        assertNotNull(dto);
        assertNull(dto.getCategoryName());
    }

    @Test
    void testGettersAndSetters() {
        TaskDTO dto = new TaskDTO();

        dto.setId("test-id");
        dto.setTitle("Test Title");
        dto.setDescription("Test Description");
        dto.setStatus(TaskStatus.COMPLETED);
        dto.setPriority(TaskPriority.CRITICAL);
        dto.setStarred(true);
        dto.setCategoryName("Work");

        LocalDateTime now = LocalDateTime.now();
        dto.setDueDate(now);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertEquals("test-id", dto.getId());
        assertEquals("Test Title", dto.getTitle());
        assertEquals("Test Description", dto.getDescription());
        assertEquals(TaskStatus.COMPLETED, dto.getStatus());
        assertEquals(TaskPriority.CRITICAL, dto.getPriority());
        assertTrue(dto.isStarred());
        assertEquals("Work", dto.getCategoryName());
        assertEquals(now, dto.getDueDate());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }
}
