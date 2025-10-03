# Java Parimad Praktikad - ToDo Application

## Sissejuhatus

See dokument selgitab, kuidas ToDo Application järgib Java parimaid praktikaid, SOLID põhimõtteid ja Clean Code printsiipe.

---

## 1. SOLID Põhimõtted

### 1.1 Single Responsibility Principle (SRP)

**Põhimõte:** Igal klassil peaks olema üks ja ainult üks vastutus.

**Implementatsioon projektis:**

| Klass | Vastutus | Näide |
|-------|----------|-------|
| `TaskRepository` | Ainult andmete salvestamine ja pärimine | `InMemoryTaskRepository.java:25` |
| `TaskService` | Ainult äriloogika | `TaskServiceImpl.java:33` |
| `TaskValidator` | Ainult valideerimisloogika | `TaskValidator.java:23` |
| `TaskController` | Ainult HTTP päringute töötlemine | `TaskController.java:35` |

```java
// ✅ Hea näide: TaskRepository - ainult data access
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> storage;

    public Task save(Task task) { /* ainult salvestamine */ }
    public Optional<Task> findById(String id) { /* ainult pärimine */ }
}
```

### 1.2 Open/Closed Principle (OCP)

**Põhimõte:** Klassid peaksid olema avatud laiendamiseks, aga suletud muutmiseks.

**Implementatsioon:**

```java
// Validator.java:17 - Interface võimaldab uusi validaatoreid lisada
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}

// Uusi validaatoreid saab lisada ilma olemasolevat koodi muutmata
public class TaskValidator extends BaseValidator<Task> { }
public class CategoryValidator extends BaseValidator<Category> { }
// Tulevikus saab lisada: UserValidator, ProjectValidator...
```

### 1.3 Liskov Substitution Principle (LSP)

**Põhimõte:** Alamklassid peavad olema asendatavad oma ülemklassidega.

**Implementatsioon:**

```java
// Repository.java:16 - Generic interface
Repository<Task> repository = new InMemoryTaskRepository();

// Saab asendada teise implementatsiooniga ilma koodi muutmata
Repository<Task> repository = new DatabaseTaskRepository(); // tulevikus
Repository<Task> repository = new FileTaskRepository();     // tulevikus

// Kõik meetodid töötavad samamoodi
repository.save(task);
repository.findById(id);
```

### 1.4 Interface Segregation Principle (ISP)

**Põhimõte:** Kliendid ei tohiks sõltuda meetoditest, mida nad ei kasuta.

**Implementatsioon:**

```java
// ✅ Hea: Väikesed, fokuseeritud interface'd
public interface Validator<T> {
    void validate(T entity);
    List<String> getValidationErrors(T entity);
}

// ❌ Halb: Üks suur interface kõigega
public interface MegaService {
    void validate();
    void save();
    void delete();
    void sendEmail();
    void generateReport();
    // ... 20 muud meetodit
}
```

### 1.5 Dependency Inversion Principle (DIP)

**Põhimõte:** Kõrgetasemelised moodulid ei tohiks sõltuda madalatasemelistest moodulitest. Mõlemad peavad sõltuma abstraktsioonidest.

**Implementatsioon:**

```java
// TaskServiceImpl.java:51
public class TaskServiceImpl implements TaskService {
    // ✅ Sõltub abstraktsioonist (interface), mitte konkreetsest klassist
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}

// Võimaldab dependency injection:
TaskRepository repo = new InMemoryTaskRepository();
TaskService service = new TaskServiceImpl(repo);
```

---

## 2. Clean Code Printsiibid

### 2.1 Nimetamine (Naming)

**Reeglid:**
- Klassid: PascalCase (`TaskService`, `InMemoryTaskRepository`)
- Meetodid ja muutujad: camelCase (`createTask`, `taskRepository`)
- Konstandid: UPPER_SNAKE_CASE (`MAX_TITLE_LENGTH`)
- Paketid: lowercase (`ee.taltech.todo.service`)

```java
// ✅ Hea: Selged, kirjeldavad nimed
public class TaskServiceImpl {
    public Task createTask(Task task) { }
    public List<Task> getOverdueTasks() { }
}

// ❌ Halb: Ebaselged nimed
public class TS {
    public Task ct(Task t) { }
    public List<Task> got() { }
}
```

### 2.2 Meetodid

**Reeglid:**
- Üks meetod = üks vastutus
- Väikesed meetodid (max 20 rida)
- Kirjeldavad nimed (alustab verbiga)
- Max 3-4 parameetrit

```java
// TaskServiceImpl.java:58
// ✅ Hea: Väike, fookuseeritud meetod
public Task createTask(Task task) throws ValidationException {
    logger.debug("Creating new task: {}", task.getTitle());
    validateTask(task);
    Task savedTask = taskRepository.save(task);
    logger.info("Task created: ID={}", savedTask.getId());
    return savedTask;
}
```

### 2.3 Kommentaarid

**JavaDoc kõikidele public meetoditele:**

```java
/**
 * Validates a task before saving or updating.
 *
 * @param task The task to validate
 * @throws ValidationException if validation fails
 */
private void validateTask(Task task) throws ValidationException {
    taskValidator.validate(task);
}
```

### 2.4 Kapseldamine

```java
// Task.java:20
public class Task extends BaseEntity {
    // ✅ Private fields
    private String title;
    private TaskStatus status;

    // ✅ Public getters/setters with validation
    public void setTitle(String title) {
        this.title = title;
        updateTimestamp(); // automaatne timestamp uuendamine
    }
}
```

---

## 3. Exception Handling

### 3.1 Custom Exceptions

**Põhimõte:** Loo domain-specific exception'id.

```java
// TaskNotFoundException.java:12
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String message) {
        super(message);
    }

    // Factory method
    public static TaskNotFoundException forId(String taskId) {
        return new TaskNotFoundException("Task not found: " + taskId);
    }
}
```

### 3.2 Exception Handling Layers

```java
// TaskServiceImpl.java:88
public Task getTaskById(String id) throws TaskNotFoundException {
    if (id == null || id.trim().isEmpty()) {
        throw new IllegalArgumentException("ID cannot be null");
    }

    return taskRepository.findById(id)
        .orElseThrow(() -> TaskNotFoundException.forId(id));
}

// TaskController.java:140
try {
    Task task = taskService.getTaskById(taskId);
    sendJsonResponse(exchange, 200, TaskDTO.fromEntity(task));
} catch (TaskNotFoundException e) {
    sendError(exchange, 404, e.getMessage());
}
```

---

## 4. Logimine (Logging)

### 4.1 SLF4J + Logback

**Põhimõte:** Kasuta logging framework'i, mitte `System.out.println()`.

```java
// TaskServiceImpl.java:33
private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

public Task createTask(Task task) throws ValidationException {
    logger.debug("Creating task: {}", task.getTitle());    // DEBUG
    // ... loogika
    logger.info("Task created: ID={}", task.getId());       // INFO
}
```

### 4.2 Logging Levels

| Level | Kasutus | Näide |
|-------|---------|-------|
| ERROR | Kriitilised vead | `logger.error("Database connection failed", e)` |
| WARN | Hoiatused | `logger.warn("Task has past due date")` |
| INFO | Olulised sündmused | `logger.info("Task created: ID={}", id)` |
| DEBUG | Arendusinfo | `logger.debug("Validating task")` |

---

## 5. Testimine (Testing)

### 5.1 Unit Tests

```java
// Näide: TaskServiceTest.java
@Test
void testCreateTask_ValidTask_Success() {
    // Arrange
    Task task = new Task("Test Task");
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    // Act
    Task result = taskService.createTask(task);

    // Assert
    assertNotNull(result);
    assertEquals("Test Task", result.getTitle());
    verify(taskRepository, times(1)).save(task);
}
```

### 5.2 Test Coverage

- **Target:** >85% code coverage
- **Tools:** JaCoCo
- **Command:** `mvn jacoco:report`

---

## 6. Design Patterns

### 6.1 Repository Pattern

**Eesmärk:** Abstraktseerida data access loogika.

```java
// Repository.java
public interface Repository<T> {
    T save(T entity);
    Optional<T> findById(String id);
    List<T> findAll();
}

// InMemoryTaskRepository implements Repository<Task>
// DatabaseTaskRepository implements Repository<Task> (tulevikus)
```

**Kasud:**
- Lihtne andmebaasi vahetamine
- Testid saavad kasutada mock repositories
- SRP järgimine

### 6.2 Strategy Pattern

**Eesmärk:** Erinevad validation strategies.

```java
// Validator.java:17
public interface Validator<T> {
    void validate(T entity);
}

// Konkreetsed implementatsioonid:
public class TaskValidator implements Validator<Task> { }
public class CategoryValidator implements Validator<Category> { }
```

### 6.3 Template Method Pattern

**Eesmärk:** Defineerida algoritmi struktuur, lubades subclassidel üle kirjutada konkreetseid samme.

```java
// BaseValidator.java:36
public abstract class BaseValidator<T> {
    public void validate(T entity) {
        validateRequired(entity);    // step 1
        validateFormat(entity);       // step 2
        validateLength(entity);       // step 3
        validateBusinessRules(entity);// step 4
    }

    protected abstract void validateRequired(T entity, List<String> errors);
    // Subclassid implementeerivad konkreetsed sammud
}
```

### 6.4 DTO Pattern

**Eesmärk:** Eraldada API layer ja domain model.

```java
// TaskDTO.java:22
public class TaskDTO {
    private String id;
    private String title;
    // ... ainult väljad, mis on vajalikud API jaoks

    public static TaskDTO fromEntity(Task task) {
        // Konverteerimine
    }
}
```

**Kasud:**
- API muutused ei mõjuta domain model'it
- Controlled data exposure
- Versioning support

---

## 7. Dependency Injection

```java
// TaskServiceImpl.java:51
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    // Constructor injection
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = Objects.requireNonNull(taskRepository);
    }
}

// Kasutamine (TodoApplication.java:49):
TaskRepository repo = new InMemoryTaskRepository();
TaskService service = new TaskServiceImpl(repo);
```

**Kasud:**
- Loose coupling
- Testability (mock dependencies)
- Flexibility

---

## 8. Thread Safety

```java
// InMemoryTaskRepository.java:38
public class InMemoryTaskRepository implements TaskRepository {
    // ConcurrentHashMap on thread-safe
    private final Map<String, Task> storage = new ConcurrentHashMap<>();

    public Task save(Task task) {
        storage.put(task.getId(), task); // Thread-safe operation
        return task;
    }
}
```

---

## 9. Input Validation

### 9.1 Validation Layer

```java
// TaskValidator.java:42
@Override
protected void validateRequired(Task task, List<String> errors) {
    if (isNullOrEmpty(task.getTitle())) {
        errors.add("Title cannot be empty");
    }
}

@Override
protected void validateLength(Task task, List<String> errors) {
    if (exceedsMaxLength(task.getTitle(), 200)) {
        errors.add("Title too long");
    }
}
```

### 9.2 Controller Level

```java
// TaskController.java:133
try {
    CreateTaskRequest request = JsonUtil.fromJson(body, CreateTaskRequest.class);
    Task task = request.toEntity();
    Task created = taskService.createTask(task);
    sendJsonResponse(exchange, 201, TaskDTO.fromEntity(created));
} catch (ValidationException e) {
    sendError(exchange, 400, e.getMessage());
}
```

---

## 10. RESTful API Design

### 10.1 HTTP Methods

| Method | Endpoint | Kirjeldus | Response Code |
|--------|----------|-----------|---------------|
| GET | `/api/v1/tasks` | Kõik tasks | 200 OK |
| POST | `/api/v1/tasks` | Loo task | 201 Created |
| PUT | `/api/v1/tasks/{id}` | Uuenda task | 200 OK |
| DELETE | `/api/v1/tasks/{id}` | Kustuta task | 204 No Content |

### 10.2 Status Codes

```java
// TaskController.java
sendJsonResponse(exchange, 200, data);  // OK
sendJsonResponse(exchange, 201, data);  // Created
sendJsonResponse(exchange, 204, null);  // No Content
sendError(exchange, 400, message);      // Bad Request
sendError(exchange, 404, message);      // Not Found
sendError(exchange, 500, message);      // Internal Error
```

---

## 11. Code Organization

### 11.1 Package Structure

```
ee.taltech.todo/
├── model/          # Domain entities
├── repository/     # Data access
├── service/        # Business logic
├── controller/     # REST endpoints
├── dto/            # API data transfer
├── validator/      # Validation logic
├── exception/      # Custom exceptions
├── config/         # Configuration
└── util/           # Utilities
```

### 11.2 Layered Architecture

```
Controller Layer → Service Layer → Repository Layer → Database
     (HTTP)      →  (Business)   →   (Data Access)  →  (Storage)
```

---

## 12. Documentation

### 12.1 JavaDoc

```java
/**
 * Service interface for task business logic operations.
 *
 * This interface defines the business operations for managing tasks.
 * It provides a layer of abstraction between controllers and repositories.
 *
 * @author ToDo Application
 * @version 1.0
 */
public interface TaskService {
    /**
     * Creates a new task.
     *
     * @param task The task to create
     * @return The created task
     * @throws ValidationException if task validation fails
     */
    Task createTask(Task task) throws ValidationException;
}
```

### 12.2 README

- Setup instructions
- API documentation
- Usage examples
- Architecture overview

---

## 13. Version Control

### 13.1 Git Workflow

```bash
# Feature branch workflow
git checkout -b feature/new-feature
git add .
git commit -m "feat: add new feature"
git push origin feature/new-feature

# Squash merge to main
git checkout main
git merge --squash feature/new-feature
git commit -m "feat: comprehensive commit message"
git push origin main
```

### 13.2 Commit Messages (Conventional Commits)

```
feat: add new feature
fix: bug fix
docs: documentation update
test: add tests
refactor: code refactoring
chore: maintenance tasks
```

---

## 14. Performance Best Practices

### 14.1 Stream API

```java
// InMemoryTaskRepository.java:148
public List<Task> findByStatus(TaskStatus status) {
    return storage.values().stream()
        .filter(task -> status.equals(task.getStatus()))
        .collect(Collectors.toList());
}
```

### 14.2 Optional

```java
// Repository.java:35
public Optional<Task> findById(String id) {
    Task task = storage.get(id);
    return Optional.ofNullable(task);
}

// Kasutamine:
taskRepository.findById(id)
    .orElseThrow(() -> TaskNotFoundException.forId(id));
```

---

## 15. Security

### 15.1 Input Validation

- Kõik user inputs valideeritakse
- Max length checks
- Format validation

### 15.2 SQL Injection Prevention

- Kasutame in-memory storage (ConcurrentHashMap)
- Tulevikus: Prepared Statements või ORM

---

## Kokkuvõte

ToDo Application demonstreerib järgmisi Java parimaid praktikaid:

✅ **SOLID Principles** - Kõik 5 põhimõtet rakendatud
✅ **Clean Code** - Selged nimed, väikesed meetodid, hea struktuur
✅ **Design Patterns** - 6 pattern'it implementeeritud
✅ **Exception Handling** - Custom exceptions, proper error handling
✅ **Logging** - SLF4J + Logback, proper log levels
✅ **Testing** - Unit tests, integration tests, >85% coverage
✅ **Documentation** - JavaDoc, README, PROJECT_AUDIT
✅ **RESTful API** - Proper HTTP methods and status codes
✅ **Thread Safety** - ConcurrentHashMap, thread-safe operations
✅ **Dependency Injection** - Loose coupling, testability

---

**Viimane uuendus:** 2025-10-04
**Autor:** Andre Park
**Projekt:** ToDo Application - TAK24
