package ee.taltech.todo.repository;

import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for InMemoryTaskRepository.
 */
class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;
    private Task task;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority(TaskPriority.HIGH);
    }

    @Test
    void testSave_ShouldStoreTask() {
        Task savedTask = repository.save(task);

        assertNotNull(savedTask);
        assertEquals(task.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
    }

    @Test
    void testFindById_WhenTaskExists_ShouldReturnTask() {
        repository.save(task);

        Optional<Task> found = repository.findById(task.getId());

        assertTrue(found.isPresent());
        assertEquals(task.getId(), found.get().getId());
    }

    @Test
    void testFindById_WhenTaskNotExists_ShouldReturnEmpty() {
        Optional<Task> found = repository.findById("non-existent-id");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll_WhenEmpty_ShouldReturnEmptyList() {
        List<Task> tasks = repository.findAll();

        assertNotNull(tasks);
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testFindAll_WhenTasksExist_ShouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        Task task3 = new Task();
        task3.setTitle("Task 3");

        repository.save(task1);
        repository.save(task2);
        repository.save(task3);

        List<Task> tasks = repository.findAll();

        assertEquals(3, tasks.size());
    }

    @Test
    void testDeleteById_WhenTaskExists_ShouldReturnTrue() {
        repository.save(task);

        boolean deleted = repository.deleteById(task.getId());

        assertTrue(deleted);
        assertFalse(repository.findById(task.getId()).isPresent());
    }

    @Test
    void testDeleteById_WhenTaskNotExists_ShouldReturnFalse() {
        boolean deleted = repository.deleteById("non-existent-id");

        assertFalse(deleted);
    }

    @Test
    void testUpdate_ShouldUpdateExistingTask() {
        repository.save(task);

        task.setTitle("Updated Title");
        task.setDescription("Updated Description");
        Task updated = repository.save(task);

        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated Description", updated.getDescription());

        Optional<Task> found = repository.findById(task.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated Title", found.get().getTitle());
    }

    @Test
    void testFindByStatus_ShouldReturnTasksWithStatus() {
        Task pending1 = new Task();
        pending1.setTitle("Pending 1");
        pending1.setStatus(TaskStatus.PENDING);

        Task pending2 = new Task();
        pending2.setTitle("Pending 2");
        pending2.setStatus(TaskStatus.PENDING);

        Task inProgress = new Task();
        inProgress.setTitle("In Progress");
        inProgress.setStatus(TaskStatus.IN_PROGRESS);

        repository.save(pending1);
        repository.save(pending2);
        repository.save(inProgress);

        List<Task> pendingTasks = repository.findByStatus(TaskStatus.PENDING);

        assertEquals(2, pendingTasks.size());
        assertTrue(pendingTasks.stream().allMatch(t -> t.getStatus() == TaskStatus.PENDING));
    }

    @Test
    void testFindByPriority_ShouldReturnTasksWithPriority() {
        Task high1 = new Task();
        high1.setTitle("High 1");
        high1.setPriority(TaskPriority.HIGH);

        Task high2 = new Task();
        high2.setTitle("High 2");
        high2.setPriority(TaskPriority.HIGH);

        Task low = new Task();
        low.setTitle("Low");
        low.setPriority(TaskPriority.LOW);

        repository.save(high1);
        repository.save(high2);
        repository.save(low);

        List<Task> highTasks = repository.findByPriority(TaskPriority.HIGH);

        assertEquals(2, highTasks.size());
        assertTrue(highTasks.stream().allMatch(t -> t.getPriority() == TaskPriority.HIGH));
    }

    @Test
    void testFindStarred_ShouldReturnOnlyStarredTasks() {
        Task starred1 = new Task();
        starred1.setTitle("Starred 1");
        starred1.setStarred(true);

        Task starred2 = new Task();
        starred2.setTitle("Starred 2");
        starred2.setStarred(true);

        Task notStarred = new Task();
        notStarred.setTitle("Not Starred");
        notStarred.setStarred(false);

        repository.save(starred1);
        repository.save(starred2);
        repository.save(notStarred);

        List<Task> starredTasks = repository.findStarred();

        assertEquals(2, starredTasks.size());
        assertTrue(starredTasks.stream().allMatch(Task::isStarred));
    }

    @Test
    void testFindOverdue_ShouldReturnOverdueTasks() {
        Task overdue1 = new Task();
        overdue1.setTitle("Overdue 1");
        overdue1.setDueDate(LocalDateTime.now().minusDays(2));
        overdue1.setStatus(TaskStatus.PENDING);

        Task overdue2 = new Task();
        overdue2.setTitle("Overdue 2");
        overdue2.setDueDate(LocalDateTime.now().minusDays(1));
        overdue2.setStatus(TaskStatus.IN_PROGRESS);

        Task notOverdue = new Task();
        notOverdue.setTitle("Not Overdue");
        notOverdue.setDueDate(LocalDateTime.now().plusDays(1));

        Task completed = new Task();
        completed.setTitle("Completed");
        completed.setDueDate(LocalDateTime.now().minusDays(1));
        completed.setStatus(TaskStatus.COMPLETED);

        repository.save(overdue1);
        repository.save(overdue2);
        repository.save(notOverdue);
        repository.save(completed);

        List<Task> overdueTasks = repository.findOverdue();

        assertEquals(2, overdueTasks.size());
        assertTrue(overdueTasks.stream().allMatch(Task::isOverdue));
    }

    @Test
    void testSearchByTitle_ShouldFindTasksByKeyword() {
        Task task1 = new Task();
        task1.setTitle("Complete homework");
        task1.setDescription("Math assignment");

        Task task2 = new Task();
        task2.setTitle("Buy groceries");
        task2.setDescription("Milk, eggs, bread");

        Task task3 = new Task();
        task3.setTitle("Complete project");
        task3.setDescription("Java programming");

        repository.save(task1);
        repository.save(task2);
        repository.save(task3);

        List<Task> results = repository.searchByTitle("complete");

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(t -> t.getTitle().contains("homework")));
        assertTrue(results.stream().anyMatch(t -> t.getTitle().contains("project")));
    }

    @Test
    void testSearchByTitle_CaseInsensitive() {
        Task task = new Task();
        task.setTitle("Important Meeting");
        repository.save(task);

        List<Task> results1 = repository.searchByTitle("important");
        List<Task> results2 = repository.searchByTitle("IMPORTANT");
        List<Task> results3 = repository.searchByTitle("ImPoRtAnT");

        assertEquals(1, results1.size());
        assertEquals(1, results2.size());
        assertEquals(1, results3.size());
    }

    @Test
    void testThreadSafety_ConcurrentSaves() throws InterruptedException {
        int threadCount = 10;
        int tasksPerThread = 10;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < tasksPerThread; j++) {
                    Task t = new Task();
                    t.setTitle("Task " + threadNum + "-" + j);
                    repository.save(t);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        List<Task> allTasks = repository.findAll();
        assertEquals(threadCount * tasksPerThread, allTasks.size());
    }
}
