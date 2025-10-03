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
6. **Singleton Pattern**: ConfigurationManager

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
- **H2 Database** - In-memory andmebaas
- **SLF4J + Logback** - Structured logging
- **JUnit 5 + Mockito** - Testing
- **Vanilla JavaScript** - Frontend
- **HTML5 + CSS3** - UI

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
│   │   │   ├── dto/           # Data transfer objects
│   │   │   ├── validator/     # Input validation
│   │   │   ├── exception/     # Custom exceptions
│   │   │   ├── config/        # Configuration
│   │   │   ├── util/          # Utilities
│   │   │   └── TodoApplication.java
│   │   └── resources/
│   │       ├── static/        # Frontend (HTML, CSS, JS)
│   │       ├── application.properties
│   │       └── logback.xml
│   └── test/
│       └── java/ee/taltech/todo/
│           ├── model/
│           ├── service/
│           ├── repository/
│           ├── validator/
│           └── integration/
├── pom.xml
├── README.md
└── PROJECT_AUDIT.md
```

## 🚀 Paigaldamine ja Käivitamine

### Eeldused

- Java 17 või uuem
- Maven 3.6+
- Veebilehitseja (Chrome, Firefox, Safari, Edge)

### 1. Projekti Kloonimine

```bash
git clone https://github.com/Andre05-vikk/ToDo-Java.git
cd ToDo-Java
```

### 2. Projekti Ehitamine

```bash
# Kompileeri ja ehita JAR fail
mvn clean package

# Või ilma testideta (kiirem)
mvn clean package -DskipTests
```

### 3. Rakenduse Käivitamine

**Variant A: Maven'iga**
```bash
mvn exec:java -Dexec.mainClass="ee.taltech.todo.TodoApplication"
```

**Variant B: JAR failiga**
```bash
java -jar target/todo-app-1.0.0.jar
```

### 4. Rakenduse Kasutamine

Pärast käivitamist:

1. **Ava brauser**: `http://localhost:8080`
2. **REST API**: `http://localhost:8080/api/v1/tasks`

Rakendus käivitub pordil **8080** (muudetav `application.properties` failis).

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
curl -X POST http://localhost:8080/api/v1/tasks \
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
curl http://localhost:8080/api/v1/tasks
```

## 🧪 Testimine

### Kõik testid

```bash
mvn test
```

### Testide katvus (JaCoCo)

```bash
mvn jacoco:report
```

Ava raport: `target/site/jacoco/index.html`

### Testide jaotus

- **Unit testid**: Model, Service, Repository, Validator kihid
- **Integration testid**: Täielik rakenduse töövoog

## 📊 Logid

Logid salvestatakse:
- **Konsool**: INFO level
- **Fail**: `logs/todo-app.log` (DEBUG level)

Logide konfiguratsioon: `src/main/resources/logback.xml`

## 🔧 Konfiguratsioon

Muuda `src/main/resources/application.properties`:

```properties
# Server port
api.port=8080

# Database
db.url=jdbc:h2:mem:tododb

# Logging
logging.level=INFO
logging.file=logs/todo-app.log
```

## 🐛 Probleemide Lahendamine

### Port on juba kasutusel

```bash
# Muuda port application.properties failis
api.port=9090
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

Andre Park - TAK24

## 📄 Litsents

See projekt on loodud õppeotstarbel.

## 🙏 Tänu

Tänan juhendajat ja kaaslasi tagasiside eest projekti arendamisel.
