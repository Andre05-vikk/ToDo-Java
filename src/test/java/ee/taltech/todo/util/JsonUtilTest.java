package ee.taltech.todo.util;

import ee.taltech.todo.dto.TaskDTO;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JsonUtil.
 */
class JsonUtilTest {

    @Test
    void testToJson_WithSimpleObject_ShouldSerialize() {
        TaskDTO dto = new TaskDTO();
        dto.setId("123");
        dto.setTitle("Test Task");
        dto.setStatus(TaskStatus.PENDING);
        dto.setPriority(TaskPriority.HIGH);

        String json = JsonUtil.toJson(dto);

        assertNotNull(json);
        assertTrue(json.contains("123"));
        assertTrue(json.contains("Test Task"));
        assertTrue(json.contains("PENDING"));
        assertTrue(json.contains("HIGH"));
    }

    @Test
    void testFromJson_WithValidJson_ShouldDeserialize() {
        String json = "{\"id\":\"123\",\"title\":\"Test Task\",\"status\":\"IN_PROGRESS\",\"priority\":\"MEDIUM\"}";

        TaskDTO dto = JsonUtil.fromJson(json, TaskDTO.class);

        assertNotNull(dto);
        assertEquals("123", dto.getId());
        assertEquals("Test Task", dto.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, dto.getStatus());
        assertEquals(TaskPriority.MEDIUM, dto.getPriority());
    }

    @Test
    void testToJson_WithLocalDateTime_ShouldSerializeCorrectly() {
        TaskDTO dto = new TaskDTO();
        dto.setId("456");
        dto.setTitle("Task with Date");
        LocalDateTime now = LocalDateTime.of(2025, 10, 4, 12, 30);
        dto.setCreatedAt(now);

        String json = JsonUtil.toJson(dto);

        assertNotNull(json);
        assertTrue(json.contains("2025-10-04"));
        assertTrue(json.contains("12:30"));
    }

    @Test
    void testFromJson_WithLocalDateTime_ShouldDeserializeCorrectly() {
        String json = "{\"id\":\"789\",\"title\":\"Task\",\"createdAt\":\"2025-10-04T12:30:00\"}";

        TaskDTO dto = JsonUtil.fromJson(json, TaskDTO.class);

        assertNotNull(dto);
        assertEquals("789", dto.getId());
        assertNotNull(dto.getCreatedAt());
        assertEquals(2025, dto.getCreatedAt().getYear());
        assertEquals(10, dto.getCreatedAt().getMonthValue());
        assertEquals(4, dto.getCreatedAt().getDayOfMonth());
    }

    @Test
    void testToJson_WithNullValues_ShouldHandleNulls() {
        TaskDTO dto = new TaskDTO();
        dto.setId("null-test");
        dto.setTitle(null);
        dto.setDescription(null);

        String json = JsonUtil.toJson(dto);

        assertNotNull(json);
        assertTrue(json.contains("null-test"));
    }

    @Test
    void testFromJson_WithInvalidJson_ShouldThrowException() {
        String invalidJson = "{invalid json}";

        assertThrows(RuntimeException.class, () -> {
            JsonUtil.fromJson(invalidJson, TaskDTO.class);
        });
    }

    @Test
    void testRoundTrip_ShouldPreserveData() {
        TaskDTO original = new TaskDTO();
        original.setId("round-trip");
        original.setTitle("Round Trip Test");
        original.setStatus(TaskStatus.COMPLETED);
        original.setPriority(TaskPriority.CRITICAL);
        original.setStarred(true);

        String json = JsonUtil.toJson(original);
        TaskDTO deserialized = JsonUtil.fromJson(json, TaskDTO.class);

        assertEquals(original.getId(), deserialized.getId());
        assertEquals(original.getTitle(), deserialized.getTitle());
        assertEquals(original.getStatus(), deserialized.getStatus());
        assertEquals(original.getPriority(), deserialized.getPriority());
        assertEquals(original.isStarred(), deserialized.isStarred());
    }
}
