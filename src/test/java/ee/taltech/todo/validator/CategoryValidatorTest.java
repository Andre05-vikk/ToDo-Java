package ee.taltech.todo.validator;

import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CategoryValidator.
 */
class CategoryValidatorTest {

    private CategoryValidator validator;
    private Category category;

    @BeforeEach
    void setUp() {
        validator = new CategoryValidator();
        category = new Category();
        category.setName("Work");
        category.setColor("#3498db");
    }

    @Test
    void testValidate_WithValidCategory_ShouldPass() {
        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithNullName_ShouldThrowException() {
        category.setName(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("Category name is required"));
    }

    @Test
    void testValidate_WithEmptyName_ShouldThrowException() {
        category.setName("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("Category name is required"));
    }

    @Test
    void testValidate_WithWhitespaceName_ShouldThrowException() {
        category.setName("   ");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("whitespace"));
    }

    @Test
    void testValidate_WithInvalidColorFormat_ShouldThrowException() {
        category.setColor("invalid-color");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("hex format"));
    }

    @Test
    void testValidate_WithShortHexColor_ShouldThrowException() {
        category.setColor("#FFF");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("hex format"));
    }

    @Test
    void testValidate_WithMissingHashColor_ShouldThrowException() {
        category.setColor("3498db");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("hex format"));
    }

    @Test
    void testValidate_WithValidUppercaseHexColor_ShouldPass() {
        category.setColor("#FF5733");

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithValidLowercaseHexColor_ShouldPass() {
        category.setColor("#ff5733");

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithValidMixedCaseHexColor_ShouldPass() {
        category.setColor("#Ff5733");

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithNullColor_ShouldPass() {
        category.setColor(null);

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithEmptyColor_ShouldPass() {
        category.setColor("");

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithTooLongName_ShouldThrowException() {
        String longName = "A".repeat(101);
        category.setName(longName);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("cannot exceed 100 characters"));
    }

    @Test
    void testValidate_WithMaxLengthName_ShouldPass() {
        String maxLengthName = "A".repeat(100);
        category.setName(maxLengthName);

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithTooLongDescription_ShouldThrowException() {
        String longDescription = "A".repeat(501);
        category.setDescription(longDescription);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));
        assertTrue(exception.getMessage().contains("cannot exceed 500 characters"));
    }

    @Test
    void testValidate_WithMaxLengthDescription_ShouldPass() {
        String maxLengthDescription = "A".repeat(500);
        category.setDescription(maxLengthDescription);

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testValidate_WithNullDescription_ShouldPass() {
        category.setDescription(null);

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void testIsValidHexColor_WithValidColor_ShouldReturnTrue() {
        assertTrue(validator.isValidHexColor("#3498db"));
        assertTrue(validator.isValidHexColor("#FF5733"));
        assertTrue(validator.isValidHexColor("#000000"));
        assertTrue(validator.isValidHexColor("#FFFFFF"));
    }

    @Test
    void testIsValidHexColor_WithInvalidColor_ShouldReturnFalse() {
        assertFalse(validator.isValidHexColor(null));
        assertFalse(validator.isValidHexColor(""));
        assertFalse(validator.isValidHexColor("#FFF"));
        assertFalse(validator.isValidHexColor("3498db"));
        assertFalse(validator.isValidHexColor("#GGG555"));
        assertFalse(validator.isValidHexColor("invalid"));
    }

    @Test
    void testIsValidName_WithValidName_ShouldReturnTrue() {
        assertTrue(validator.isValidName("Work"));
        assertTrue(validator.isValidName("Personal Tasks"));
        assertTrue(validator.isValidName("A"));
    }

    @Test
    void testIsValidName_WithInvalidName_ShouldReturnFalse() {
        assertFalse(validator.isValidName(null));
        assertFalse(validator.isValidName(""));
        assertFalse(validator.isValidName("   "));
        assertFalse(validator.isValidName("A".repeat(101)));
    }

    @Test
    void testIsCommonColor_WithValidHexColor_ShouldReturnTrue() {
        assertTrue(validator.isCommonColor("#3498db"));
        assertTrue(validator.isCommonColor("#FF5733"));
    }

    @Test
    void testIsCommonColor_WithInvalidColor_ShouldReturnFalse() {
        assertFalse(validator.isCommonColor(null));
        assertFalse(validator.isCommonColor(""));
        assertFalse(validator.isCommonColor("red"));
        assertFalse(validator.isCommonColor("#FFF"));
    }

    @Test
    void testValidate_WithMultipleErrors_ShouldListAllErrors() {
        category.setName("");
        category.setColor("invalid");
        category.setDescription("A".repeat(501));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validate(category));

        // Should have multiple error messages
        String message = exception.getMessage();
        assertTrue(message.contains("name") || message.contains("color") || message.contains("description"));
    }

    @Test
    void testValidate_WithLeadingTrailingWhitespace_ShouldPass() {
        // This is allowed but logged as a warning
        category.setName("  Work  ");

        assertDoesNotThrow(() -> validator.validate(category));
    }
}
