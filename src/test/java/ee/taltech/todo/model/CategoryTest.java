package ee.taltech.todo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Category entity.
 */
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void testCategoryCreation_ShouldGenerateIdAndTimestamps() {
        assertNotNull(category.getId(), "Category ID should be generated");
        assertNotNull(category.getCreatedAt(), "Created timestamp should be set");
        assertNotNull(category.getUpdatedAt(), "Updated timestamp should be set");
    }

    @Test
    void testSetName_ShouldUpdateName() {
        String name = "Work";
        category.setName(name);
        assertEquals(name, category.getName());
    }

    @Test
    void testSetColor_ShouldUpdateColor() {
        String color = "#FF5733";
        category.setColor(color);
        assertEquals(color, category.getColor());
    }

    @Test
    void testCategoryWithAllFields_ShouldStoreAllData() {
        String name = "Personal";
        String color = "#3498db";

        category.setName(name);
        category.setColor(color);

        assertEquals(name, category.getName());
        assertEquals(color, category.getColor());
    }

    @Test
    void testMultipleCategoryInstances_ShouldHaveUniqueIds() {
        Category cat1 = new Category();
        Category cat2 = new Category();
        Category cat3 = new Category();

        assertNotEquals(cat1.getId(), cat2.getId(), "Categories should have unique IDs");
        assertNotEquals(cat1.getId(), cat3.getId(), "Categories should have unique IDs");
        assertNotEquals(cat2.getId(), cat3.getId(), "Categories should have unique IDs");
    }

    @Test
    void testSetName_WithEmptyString_ShouldStoreEmptyString() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testSetColor_WithDifferentFormats_ShouldStoreValue() {
        // Hex color
        category.setColor("#FF0000");
        assertEquals("#FF0000", category.getColor());

        // RGB color
        category.setColor("rgb(255, 0, 0)");
        assertEquals("rgb(255, 0, 0)", category.getColor());
    }
}
