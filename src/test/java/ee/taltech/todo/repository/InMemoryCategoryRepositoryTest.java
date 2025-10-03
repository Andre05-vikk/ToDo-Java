package ee.taltech.todo.repository;

import ee.taltech.todo.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InMemoryCategoryRepository.
 */
class InMemoryCategoryRepositoryTest {

    private InMemoryCategoryRepository repository;
    private Category category;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCategoryRepository();
        category = new Category();
        category.setName("Work");
        category.setColor("#3498db");
    }

    @Test
    void testSave_ShouldStoreCategory() {
        Category saved = repository.save(category);

        assertNotNull(saved);
        assertEquals(category.getId(), saved.getId());
        assertEquals(category.getName(), saved.getName());
    }

    @Test
    void testFindById_WhenCategoryExists_ShouldReturnCategory() {
        repository.save(category);

        Optional<Category> found = repository.findById(category.getId());

        assertTrue(found.isPresent());
        assertEquals(category.getId(), found.get().getId());
    }

    @Test
    void testFindById_WhenCategoryNotExists_ShouldReturnEmpty() {
        Optional<Category> found = repository.findById("non-existent-id");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll_WhenEmpty_ShouldReturnEmptyList() {
        List<Category> categories = repository.findAll();

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void testFindAll_WhenCategoriesExist_ShouldReturnAllCategories() {
        Category cat1 = new Category();
        cat1.setName("Work");
        Category cat2 = new Category();
        cat2.setName("Personal");
        Category cat3 = new Category();
        cat3.setName("Shopping");

        repository.save(cat1);
        repository.save(cat2);
        repository.save(cat3);

        List<Category> categories = repository.findAll();

        assertEquals(3, categories.size());
    }

    @Test
    void testDeleteById_WhenCategoryExists_ShouldReturnTrue() {
        repository.save(category);

        boolean deleted = repository.deleteById(category.getId());

        assertTrue(deleted);
        assertFalse(repository.findById(category.getId()).isPresent());
    }

    @Test
    void testDeleteById_WhenCategoryNotExists_ShouldReturnFalse() {
        boolean deleted = repository.deleteById("non-existent-id");

        assertFalse(deleted);
    }

    @Test
    void testUpdate_ShouldUpdateExistingCategory() {
        repository.save(category);

        category.setName("Updated Work");
        category.setColor("#FF5733");
        Category updated = repository.save(category);

        assertEquals("Updated Work", updated.getName());
        assertEquals("#FF5733", updated.getColor());

        Optional<Category> found = repository.findById(category.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated Work", found.get().getName());
    }

    @Test
    void testFindByName_WhenCategoryExists_ShouldReturnCategory() {
        repository.save(category);

        Optional<Category> found = repository.findByName("Work");

        assertTrue(found.isPresent());
        assertEquals("Work", found.get().getName());
    }

    @Test
    void testFindByName_WhenCategoryNotExists_ShouldReturnEmpty() {
        Optional<Category> found = repository.findByName("NonExistent");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindByName_CaseSensitive() {
        repository.save(category);

        Optional<Category> found = repository.findByName("work");

        assertFalse(found.isPresent(), "Search should be case-sensitive");
    }

    @Test
    void testThreadSafety_ConcurrentSaves() throws InterruptedException {
        int threadCount = 10;
        int categoriesPerThread = 10;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < categoriesPerThread; j++) {
                    Category c = new Category();
                    c.setName("Category " + threadNum + "-" + j);
                    c.setColor("#000000");
                    repository.save(c);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        List<Category> allCategories = repository.findAll();
        assertEquals(threadCount * categoriesPerThread, allCategories.size());
    }
}
