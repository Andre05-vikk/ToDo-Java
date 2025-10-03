package ee.taltech.todo.service;

import ee.taltech.todo.exception.CategoryNotFoundException;
import ee.taltech.todo.exception.DuplicateEntityException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;
import ee.taltech.todo.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CategoryServiceImpl using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    private CategoryServiceImpl categoryService;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository);
        category = new Category();
        category.setName("Work");
        category.setColor("#3498db");
    }

    @Test
    void testConstructor_WithNullRepository_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> new CategoryServiceImpl(null));
    }

    @Test
    void testCreateCategory_WithValidCategory_ShouldSaveCategory() throws ValidationException, DuplicateEntityException {
        when(categoryRepository.existsByName("Work")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category created = categoryService.createCategory(category);

        assertNotNull(created);
        verify(categoryRepository, times(1)).existsByName("Work");
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testCreateCategory_WithDuplicateName_ShouldThrowException() {
        when(categoryRepository.existsByName("Work")).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> categoryService.createCategory(category));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testCreateCategory_WithInvalidCategory_ShouldThrowValidationException() {
        category.setName(null);

        assertThrows(ValidationException.class, () -> categoryService.createCategory(category));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_WithValidCategory_ShouldUpdateCategory()
            throws CategoryNotFoundException, ValidationException, DuplicateEntityException {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);
        when(categoryRepository.findByName("Work")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category updated = categoryService.updateCategory(category);

        assertNotNull(updated);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testUpdateCategory_WhenCategoryNotExists_ShouldThrowException() {
        when(categoryRepository.existsById(category.getId())).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(category));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testUpdateCategory_WithDuplicateName_ShouldThrowException() {
        Category existingCategory = new Category();
        existingCategory.setName("Work");

        when(categoryRepository.existsById(category.getId())).thenReturn(true);
        when(categoryRepository.findByName("Work")).thenReturn(Optional.of(existingCategory));

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(category));
    }

    @Test
    void testUpdateCategory_WithSameCategory_ShouldUpdate()
            throws CategoryNotFoundException, ValidationException, DuplicateEntityException {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);
        when(categoryRepository.findByName("Work")).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category updated = categoryService.updateCategory(category);

        assertNotNull(updated);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testGetCategoryById_WhenCategoryExists_ShouldReturnCategory() throws CategoryNotFoundException {
        when(categoryRepository.findById("123")).thenReturn(Optional.of(category));

        Category found = categoryService.getCategoryById("123");

        assertNotNull(found);
        assertEquals(category.getName(), found.getName());
        verify(categoryRepository, times(1)).findById("123");
    }

    @Test
    void testGetCategoryById_WhenCategoryNotExists_ShouldThrowException() {
        when(categoryRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryById("999"));
    }

    @Test
    void testGetCategoryById_WithNullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(null));
    }

    @Test
    void testGetCategoryById_WithEmptyId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryById(""));
    }

    @Test
    void testGetCategoryByName_WhenCategoryExists_ShouldReturnCategory() throws CategoryNotFoundException {
        when(categoryRepository.findByName("Work")).thenReturn(Optional.of(category));

        Category found = categoryService.getCategoryByName("Work");

        assertNotNull(found);
        assertEquals("Work", found.getName());
        verify(categoryRepository, times(1)).findByName("Work");
    }

    @Test
    void testGetCategoryByName_WhenCategoryNotExists_ShouldThrowException() {
        when(categoryRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategoryByName("NonExistent"));
    }

    @Test
    void testGetCategoryByName_WithNullName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryByName(null));
    }

    @Test
    void testGetCategoryByName_WithEmptyName_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.getCategoryByName(""));
    }

    @Test
    void testGetAllCategories_ShouldReturnAllCategories() {
        Category category1 = new Category();
        category1.setName("Work");
        Category category2 = new Category();
        category2.setName("Personal");

        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testDeleteCategory_WhenCategoryExists_ShouldDeleteCategory() throws CategoryNotFoundException {
        String categoryId = "123";

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.deleteById(categoryId)).thenReturn(true);

        assertDoesNotThrow(() -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategory_WhenCategoryNotExists_ShouldThrowException() {
        String categoryId = "999";

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, never()).deleteById(anyString());
    }

    @Test
    void testDeleteCategory_WithNullId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(null));
    }

    @Test
    void testDeleteCategory_WithEmptyId_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(""));
    }

    @Test
    void testExistsByName_WhenExists_ShouldReturnTrue() {
        when(categoryRepository.existsByName("Work")).thenReturn(true);

        boolean exists = categoryService.existsByName("Work");

        assertTrue(exists);
        verify(categoryRepository, times(1)).existsByName("Work");
    }

    @Test
    void testExistsByName_WhenNotExists_ShouldReturnFalse() {
        when(categoryRepository.existsByName("NonExistent")).thenReturn(false);

        boolean exists = categoryService.existsByName("NonExistent");

        assertFalse(exists);
        verify(categoryRepository, times(1)).existsByName("NonExistent");
    }

    @Test
    void testExistsByName_WithNullName_ShouldReturnFalse() {
        boolean exists = categoryService.existsByName(null);

        assertFalse(exists);
        verify(categoryRepository, never()).existsByName(anyString());
    }

    @Test
    void testExistsByName_WithEmptyName_ShouldReturnFalse() {
        boolean exists = categoryService.existsByName("");

        assertFalse(exists);
        verify(categoryRepository, never()).existsByName(anyString());
    }

    @Test
    void testGetTotalCount_ShouldReturnCount() {
        when(categoryRepository.count()).thenReturn(5L);

        long count = categoryService.getTotalCount();

        assertEquals(5L, count);
        verify(categoryRepository, times(1)).count();
    }

    @Test
    void testGetTotalCount_WhenEmpty_ShouldReturnZero() {
        when(categoryRepository.count()).thenReturn(0L);

        long count = categoryService.getTotalCount();

        assertEquals(0L, count);
        verify(categoryRepository, times(1)).count();
    }
}
