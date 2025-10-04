# ToDo Application

Professionaalne ülesannete haldamise rakendus, mis demonstreerib Java parimaid praktikaid, objektorienteeritud programmeerimise põhimõtteid ja keerukamaid disainilahendusi.

## 📋 Projekti Kirjeldus

ToDo Application on täisfunktsionaalne ülesannete haldamise süsteem, mis pakub nii REST API kui ka web-põhist kasutajaliidest. Rakendus on ehitatud mitmekihilise arhitektuuriga, kasutades tuntud disainimustreid ja Java parimaid praktikaid.

### Põhifunktsioonid

- ✅ **Ülesannete haldamine**: Loo, muuda, kustuta ja vaata ülesandeid
- 📁 **Kategooriad**: Organiseeri ülesandeid kategooriate kaupa
- ⭐ **Prioriteedid**: 4-tasemeline prioriteedi süsteem (LOW, MEDIUM, HIGH, CRITICAL)
- 📅 **Tähtajad**: Sea ja jälgi ülesannete tähtaegu
- 🔍 **Otsing ja filtreerimine**: Otsi ja filtreeri ülesandeid staatuse, prioriteedi või kategooria järgi
- ⭐ **Tärniga märgistamine**: Märgi olulised ülesanded tärniga
- 📊 **Statistika**: Vaata ülesannete arvu staatuste kaupa

## 🏗️ Arhitektuur ja Design Patterns

Rakendus demonstreerib järgmisi OOP põhimõtteid ja disainimustreid:

### OOP Põhimõtted
- **Pärilikkus (Inheritance)**: BaseEntity → Task, Category
- **Kompositsioon (Composition)**: Service → Repository, Task → Category
- **Kapseldamine (Encapsulation)**: Private fields, public methods
- **Abstraktsioon**: Abstract classes ja interface'd

### Design Patterns
1. **Repository Pattern**: Data access layer abstraktsioon
2. **Strategy Pattern**: Validation strategies (TaskValidator, CategoryValidator)
3. **Template Method Pattern**: BaseValidator validation flow
4. **DTO Pattern**: Data Transfer Objects API kihis
5. **Dependency Injection**: Constructor-based injection

### Arhitektuurikihid
```
Presentation Layer (REST API + Frontend)
         ↓
Service Layer (Business Logic)
         ↓
Repository Layer (Data Access)
         ↓
Domain Model Layer (Entities)
```

## 🛠️ Tehnoloogiad

- **Java 17+**
- **Maven** - Dependency management
- **ConcurrentHashMap** - Thread-safe in-memory storage
- **SLF4J + Logback** - Structured logging
- **JUnit 5 + Mockito** - Testing
- **Gson** - JSON serialization
- **Vanilla JavaScript** - Frontend
- **HTML5 + CSS3** - UI
- **com.sun.net.httpserver** - Built-in HTTP server

## 📦 Projekti Struktuur

```
ToDo-Java/
├── src/
│   ├── main/
│   │   ├── java/ee/taltech/todo/
│   │   │   ├── model/          # Domain entities
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── service/        # Business logic
│   │   │   ├── controller/     # REST API endpoints
│   │   │   ├── dto/            # Data transfer objects
│   │   │   ├── validator/      # Input validation
│   │   │   ├── exception/      # Custom exceptions
│   │   │   ├── util/           # Utilities
│   │   │   └── TodoApplication.java
│   │   └── resources/
│   │       ├── static/         # Frontend (HTML, CSS, JS)
│   │       └── logback.xml
│   └── test/
│       └── java/ee/taltech/todo/
│           ├── model/          # Entity tests
│           ├── service/        # Service layer tests
│           ├── repository/     # Repository tests
│           ├── validator/      # Validation tests
│           ├── dto/            # DTO tests
│           ├── exception/      # Exception tests
│           └── util/           # Utility tests
├── pom.xml
├── README.md
├── PROJECT_AUDIT.md
└── JAVA_BEST_PRACTICES.md
```

## 🚀 Paigaldamine ja Käivitamine

### Eeldused

- Java 17 või uuem
- Maven 3.6+
- Veebilehitseja (Chrome, Firefox, Safari, Edge)


### 1. Projekti Ehitamine

```bash
# Kompileeri ja ehita JAR fail
mvn clean package

# Või ilma testideta (kiirem)
mvn clean package -DskipTests
```

### 2. Rakenduse Käivitamine

**Variant A: Maven'iga**
```bash
mvn exec:java -Dexec.mainClass="ee.taltech.todo.TodoApplication"
```

**Variant B: JAR failiga**
```bash
java -jar target/todo-app-1.0.0.jar
```

### 3. Rakenduse Kasutamine

Pärast käivitamist:

1. **Web UI**: `http://localhost:8081` - Interaktiivne kasutajaliides
2. **REST API**: `http://localhost:8081/api/v1/tasks` - API otspunktid

Rakendus käivitub pordil **8081**.

**Web UI funktsioonid:**
- ✨ Lisa uusi ülesandeid vormiga
- 🔍 Filtreeri ülesandeid staatuse, prioriteedi, tärniga või hilinenud ülesannete järgi
- 📊 Vaata reaalajas statistikat
- ✏️ Muuda ülesandeid modal aknas
- 📁 Halda kategooriaid visuaalselt
- ⭐ Märgi olulised ülesanded tärniga

## 📖 REST API Dokumentatsioon

### Tasks Endpoints

| Meetod | Endpoint | Kirjeldus |
|--------|----------|-----------|
| GET | `/api/v1/tasks` | Kõik ülesanded |
| GET | `/api/v1/tasks/{id}` | Ülesanne ID järgi |
| POST | `/api/v1/tasks` | Loo uus ülesanne |
| PUT | `/api/v1/tasks/{id}` | Uuenda ülesannet |
| DELETE | `/api/v1/tasks/{id}` | Kustuta ülesanne |
| GET | `/api/v1/tasks/status/{status}` | Ülesanded staatuse järgi |
| GET | `/api/v1/tasks/priority/{priority}` | Ülesanded prioriteedi järgi |
| GET | `/api/v1/tasks/search?q={keyword}` | Otsi ülesandeid |
| POST | `/api/v1/tasks/{id}/complete` | Märgi ülesanne tehtuks |
| POST | `/api/v1/tasks/{id}/start` | Alusta ülesannet |

### Categories Endpoints

| Meetod | Endpoint | Kirjeldus |
|--------|----------|-----------|
| GET | `/api/v1/categories` | Kõik kategooriad |
| GET | `/api/v1/categories/{id}` | Kategooria ID järgi |
| POST | `/api/v1/categories` | Loo uus kategooria |
| PUT | `/api/v1/categories/{id}` | Uuenda kategooriat |
| DELETE | `/api/v1/categories/{id}` | Kustuta kategooria |

### Näited

**Loo uus ülesanne:**
```bash
curl -X POST http://localhost:8081/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tee kodutöö",
    "description": "Programmeerimine Java",
    "priority": "HIGH",
    "dueDate": "2025-10-15T18:00:00"
  }'
```

**Hangi kõik ülesanded:**
```bash
curl http://localhost:8081/api/v1/tasks
```

## 🧪 Testimine

Rakendus sisaldab põhjalikku automaattestide komplekti rakenduse töö ja töökindluse kontrollimiseks.

### Kõigi testide käivitamine

```bash
# Käivita kõik testid
mvn test

# Käivita testid ja genereeri coverage raport
mvn clean test jacoco:report

# Vaata coverage raportit brauseris
open target/site/jacoco/index.html
```

### Testide statistika

- **Testide arv**: 176
- **Test pass rate**: 100% (kõik testid läbivad)
- **Koodikattuvus**: 44%

### Testide jaotus pakettide kaupa

| Pakett | Testide arv | Coverage |
|--------|-------------|----------|
| **Exception** | 20 | 100% |
| **Service** | 46 | 74% |
| **Validator** | 26 | 87% |
| **Util** | 7 | 88% |
| **DTO** | 6 | 55% |
| **Repository** | 28 | 51% |
| **Model** | 43 | 42% |

### Testi tüübid

**1. Entity testid** (Model layer)
- `TaskTest.java` - Task entity funktsioonid ja äriloogika
- `CategoryTest.java` - Category entity funktsioonid

**2. Repository testid** (Data access layer)
- `InMemoryTaskRepositoryTest.java` - Andmete salvestamine, pärimine, thread safety
- `InMemoryCategoryRepositoryTest.java` - Kategooriate haldamine, thread safety

**3. Service testid** (Business logic)
- `TaskServiceImplTest.java` - Ülesannete äriloogika, Mockito-põhised testid
- `CategoryServiceImplTest.java` - Kategooriate äriloogika, validatsioon

**4. Validator testid** (Input validation)
- `TaskValidatorTest.java` - Sisendi valideerimisreeglid
- `CategoryValidatorTest.java` - Kategooriate validatsioon, hex värvid

**5. DTO testid** (Data Transfer Objects)
- `TaskDTOTest.java` - Entity → DTO mapping
- `CategoryDTOTest.java` - Category mapping

**6. Exception testid** (Error handling)
- `TaskNotFoundExceptionTest.java`
- `CategoryNotFoundExceptionTest.java`
- `DuplicateEntityExceptionTest.java`
- `ValidationExceptionTest.java`

**7. Utility testid**
- `JsonUtilTest.java` - JSON serialization/deserialization, LocalDateTime handling

### Coverage raport

Coverage raport genereeritakse JaCoCo abil ja sisaldab:
- Line coverage (rea katvus)
- Branch coverage (hargnemiste katvus)
- Method coverage (meetodite katvus)
- Class coverage (klasside katvus)

```bash
# Genereeri ja ava raport
mvn jacoco:report && open target/site/jacoco/index.html
```

### Käivita konkreetseid teste

```bash
# Ainult model testid
mvn test -Dtest="ee.taltech.todo.model.*Test"

# Ainult service testid
mvn test -Dtest="ee.taltech.todo.service.*Test"

# Konkreetne test klass
mvn test -Dtest="TaskServiceImplTest"

# Konkreetne test meetod
mvn test -Dtest="TaskServiceImplTest#testCreateTask_WithValidTask_ShouldSaveTask"
```

## 📊 Logid

Logid salvestatakse:
- **Konsool**: INFO level
- **Fail**: `logs/todo-app.log` (DEBUG level)

Logide konfiguratsioon: `src/main/resources/logback.xml`

## 🔧 Konfiguratsioon

Serveri port on määratud `TodoApplication.java` failis:

```java
private static final int PORT = 8081;
```

Kui soovid kasutada teist porti, muuda seda konstanti ja uuenda ka `app.js` failis API_BASE URL.

**Andmete salvestamine**: Rakendus kasutab in-memory ConcurrentHashMap andmete salvestamiseks.
Andmed kustutatakse rakenduse taaskäivitamisel. See on mõeldud demonstreerimiseks ja testimiseks.

**Logide konfiguratsioon**: `src/main/resources/logback.xml`

## 🐛 Probleemide Lahendamine

### Port on juba kasutusel

```bash
# Muuda PORT konstanti TodoApplication.java failis
private static final int PORT = 9090;

# Ja uuenda ka frontend app.js failis
const API_BASE = 'http://localhost:9090/api/v1';
```

### Java versioon on vale

```bash
# Kontrolli Java versiooni
java -version

# Peab olema 17+
```

### Maven build ebaõnnestub

```bash
# Puhasta ja ehita uuesti
mvn clean install -U
```

## 📝 Java Parimad Praktikad

Vaata detailset selgitust failist: **JAVA_BEST_PRACTICES.md**

## 🏆 Projekti Audit

Põhjalik disaini ja analüüsi dokument: **PROJECT_AUDIT.md**

## 👨‍💻 Autor

Andre Park 

## 🙏 Tänu

Tänan juhendajat ja kaaslasi tagasiside eest projekti arendamisel.
