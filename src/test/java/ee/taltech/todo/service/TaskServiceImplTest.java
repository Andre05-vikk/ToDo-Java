package ee.taltech.todo.service;

import ee.taltech.todo.exception.TaskNotFoundException;
import ee.taltech.todo.exception.ValidationException;
import ee.taltech.todo.model.Category;
import ee.taltech.todo.model.Task;
import ee.taltech.todo.model.TaskPriority;
import ee.taltech.todo.model.TaskStatus;
import ee.taltech.todo.repository.CategoryRepository;
import ee.taltech.todo.repository.TaskRepository;
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
 * Unit tests for TaskServiceImpl using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private TaskServiceImpl taskService;
    private Task task;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository, categoryRepository);
        task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setPriority(TaskPriority.MEDIUM);
    }

    @Test
    void testCreateTask_WithValidTask_ShouldSaveTask() throws ValidationException {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task created = taskService.createTask(task);

        assertNotNull(created);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCreateTask_WithInvalidTask_ShouldThrowValidationException() {
        task.setTitle(null);

        assertThrows(ValidationException.class, () -> taskService.createTask(task));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testGetTaskById_WhenTaskExists_ShouldReturnTask() throws TaskNotFoundException {
        when(taskRepository.findById("123")).thenReturn(Optional.of(task));

        Task found = taskService.getTaskById("123");

        assertNotNull(found);
        assertEquals(task.getTitle(), found.getTitle());
        verify(taskRepository, times(1)).findById("123");
    }

    @Test
    void testGetTaskById_WhenTaskNotExists_ShouldThrowException() {
        when(taskRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById("999"));
    }

    @Test
    void testGetAllTasks_ShouldReturnAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testUpdateTask_WithValidTask_ShouldUpdateTask() throws TaskNotFoundException, ValidationException {
        when(taskRepository.existsById(task.getId())).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updated = taskService.updateTask(task);

        assertNotNull(updated);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask_WhenTaskNotExists_ShouldThrowException() {
        when(taskRepository.existsById(task.getId())).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(task));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTask_WhenTaskExists_ShouldDeleteTask() throws TaskNotFoundException {
        String taskId = "123";

        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(taskRepository.deleteById(taskId)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(taskId));
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testDeleteTask_WhenTaskNotExists_ShouldThrowException() {
        String taskId = "999";

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));
    }

    @Test
    void testCompleteTask_ShouldChangeStatusToCompleted() throws TaskNotFoundException {
        String taskId = task.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task completed = taskService.completeTask(taskId);

        assertEquals(TaskStatus.COMPLETED, completed.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testStartTask_ShouldChangeStatusToInProgress() throws TaskNotFoundException {
        String taskId = task.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task started = taskService.startTask(taskId);

        assertEquals(TaskStatus.IN_PROGRESS, started.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCancelTask_ShouldChangeStatusToCancelled() throws TaskNotFoundException {
        String taskId = task.getId();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task cancelled = taskService.cancelTask(taskId);

        assertEquals(TaskStatus.CANCELLED, cancelled.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testGetTasksByStatus_ShouldReturnFilteredTasks() {
        Task task1 = new Task();
        task1.setStatus(TaskStatus.PENDING);
        Task task2 = new Task();
        task2.setStatus(TaskStatus.PENDING);

        List<Task> pendingTasks = Arrays.asList(task1, task2);
        when(taskRepository.findByStatus(TaskStatus.PENDING)).thenReturn(pendingTasks);

        List<Task> result = taskService.getTasksByStatus(TaskStatus.PENDING);

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByStatus(TaskStatus.PENDING);
    }

    @Test
    void testGetTasksByPriority_ShouldReturnFilteredTasks() {
        Task task1 = new Task();
        task1.setPriority(TaskPriority.HIGH);
        Task task2 = new Task();
        task2.setPriority(TaskPriority.HIGH);

        List<Task> highTasks = Arrays.asList(task1, task2);
        when(taskRepository.findByPriority(TaskPriority.HIGH)).thenReturn(highTasks);

        List<Task> result = taskService.getTasksByPriority(TaskPriority.HIGH);

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByPriority(TaskPriority.HIGH);
    }

    @Test
    void testGetStarredTasks_ShouldReturnStarredTasks() {
        Task task1 = new Task();
        task1.setStarred(true);

        List<Task> starredTasks = Arrays.asList(task1);
        when(taskRepository.findStarred()).thenReturn(starredTasks);

        List<Task> result = taskService.getStarredTasks();

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findStarred();
    }

    @Test
    void testGetOverdueTasks_ShouldReturnOverdueTasks() {
        List<Task> overdueTasks = Arrays.asList(task);
        when(taskRepository.findOverdue()).thenReturn(overdueTasks);

        List<Task> result = taskService.getOverdueTasks();

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findOverdue();
    }

    @Test
    void testSearchTasks_ShouldReturnMatchingTasks() {
        List<Task> searchResults = Arrays.asList(task);
        when(taskRepository.searchByTitle("test")).thenReturn(searchResults);

        List<Task> result = taskService.searchTasks("test");

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).searchByTitle("test");
    }

    @Test
    void testAssignCategory_WhenCategoryExists_ShouldAssignCategory() throws TaskNotFoundException {
        String taskId = task.getId();
        String categoryId = "456";

        Category category = new Category();
        category.setName("Work");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.assignCategory(taskId, categoryId);

        assertEquals(category, result.getCategory());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testAssignCategory_WhenTaskNotExists_ShouldThrowException() {
        when(taskRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () ->
            taskService.assignCategory("999", "456"));
    }
}
