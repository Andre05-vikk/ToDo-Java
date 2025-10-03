package ee.taltech.todo.validator;

import ee.taltech.todo.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Validator for Category entities.
 *
 * Implements specific validation rules for categories using the Strategy pattern.
 * Extends BaseValidator to utilize the Template Method pattern.
 *
 * Design Patterns:
 * - Strategy Pattern: Concrete validation strategy for Category
 * - Template Method Pattern: Uses BaseValidator's template methods
 *
 * @author ToDo Application
 * @version 1.0
 */
public class CategoryValidator extends BaseValidator<Category> {

    private static final Logger logger = LoggerFactory.getLogger(CategoryValidator.class);

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Override
    protected void validateRequired(Category category, List<String> errors) {
        logger.debug("Validating required fields for category");

        // Name is required
        if (isNullOrEmpty(category.getName())) {
            errors.add("Category name is required and cannot be empty");
        }
    }

    @Override
    protected void validateFormat(Category category, List<String> errors) {
        logger.debug("Validating field formats for category");

        // Validate color format if present
        if (category.getColor() != null && !category.getColor().isEmpty()) {
            if (!isValidHexColor(category.getColor())) {
                errors.add(String.format("Category color must be in hex format (#RRGGBB), got: %s",
                        category.getColor()));
            }
        }
    }

    @Override
    protected void validateLength(Category category, List<String> errors) {
        logger.debug("Validating field lengths for category");

        // Validate name length
        if (exceedsMaxLength(category.getName(), MAX_NAME_LENGTH)) {
            errors.add(String.format("Category name cannot exceed %d characters (current: %d)",
                    MAX_NAME_LENGTH, category.getName().length()));
        }

        // Validate description length if present
        if (exceedsMaxLength(category.getDescription(), MAX_DESCRIPTION_LENGTH)) {
            errors.add(String.format("Category description cannot exceed %d characters (current: %d)",
                    MAX_DESCRIPTION_LENGTH, category.getDescription().length()));
        }
    }

    @Override
    protected void validateBusinessRules(Category category, List<String> errors) {
        logger.debug("Validating business rules for category");

        // Ensure name doesn't contain only whitespace
        if (category.getName() != null && category.getName().trim().isEmpty()) {
            errors.add("Category name cannot contain only whitespace");
        }

        // Ensure name doesn't start or end with whitespace
        if (category.getName() != null &&
            (!category.getName().equals(category.getName().trim()))) {
            logger.debug("Category name has leading/trailing whitespace - will be trimmed");
            // This is just a warning, we could auto-trim in the service
        }
    }

    /**
     * Validates that a color is in valid hex format (#RRGGBB).
     *
     * @param color The color to validate
     * @return true if color is valid hex format
     */
    public boolean isValidHexColor(String color) {
        if (color == null || color.isEmpty()) {
            return false;
        }

        return HEX_COLOR_PATTERN.matcher(color).matches();
    }

    /**
     * Validates that a category name is valid.
     *
     * @param name The name to validate
     * @return true if name is valid
     */
    public boolean isValidName(String name) {
        if (isNullOrEmpty(name)) {
            return false;
        }

        if (exceedsMaxLength(name, MAX_NAME_LENGTH)) {
            return false;
        }

        // Name should not be only whitespace
        if (name.trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Validates common hex colors.
     * Helper method to check if a color is a valid common color.
     *
     * @param color The color to check
     * @return true if it's a recognized color format
     */
    public boolean isCommonColor(String color) {
        if (color == null) {
            return false;
        }

        // Check if it's a valid hex color
        if (isValidHexColor(color)) {
            return true;
        }

        // Could add support for named colors (red, blue, etc.) if needed
        // For now, only support hex colors

        return false;
    }
}
