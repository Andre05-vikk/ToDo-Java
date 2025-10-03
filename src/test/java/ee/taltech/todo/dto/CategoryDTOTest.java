package ee.taltech.todo.dto;

import ee.taltech.todo.model.Category;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CategoryDTO.
 */
class CategoryDTOTest {

    @Test
    void testFromEntity_ShouldMapAllFields() {
        Category category = new Category();
        category.setName("Work");
        category.setColor("#3498db");

        CategoryDTO dto = CategoryDTO.fromEntity(category);

        assertNotNull(dto);
        assertEquals(category.getId(), dto.getId());
        assertEquals("Work", dto.getName());
        assertEquals("#3498db", dto.getColor());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
    }

    @Test
    void testGettersAndSetters() {
        CategoryDTO dto = new CategoryDTO();

        dto.setId("test-id");
        dto.setName("Personal");
        dto.setColor("#FF5733");

        LocalDateTime now = LocalDateTime.now();
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertEquals("test-id", dto.getId());
        assertEquals("Personal", dto.getName());
        assertEquals("#FF5733", dto.getColor());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }
}
