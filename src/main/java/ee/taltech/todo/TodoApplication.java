package ee.taltech.todo;

import com.sun.net.httpserver.HttpServer;
import ee.taltech.todo.controller.CategoryController;
import ee.taltech.todo.controller.TaskController;
import ee.taltech.todo.repository.CategoryRepository;
import ee.taltech.todo.repository.InMemoryCategoryRepository;
import ee.taltech.todo.repository.InMemoryTaskRepository;
import ee.taltech.todo.repository.TaskRepository;
import ee.taltech.todo.service.CategoryService;
import ee.taltech.todo.service.CategoryServiceImpl;
import ee.taltech.todo.service.TaskService;
import ee.taltech.todo.service.TaskServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main application class for ToDo Application.
 *
 * Initializes all components and starts the HTTP server.
 * Demonstrates dependency injection and application bootstrapping.
 *
 * @author ToDo Application
 * @version 1.0
 */
public class TodoApplication {

    private static final Logger logger = LoggerFactory.getLogger(TodoApplication.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        logger.info("Starting ToDo Application...");

        try {
            // Initialize components (Dependency Injection manually)
            TodoApplication app = new TodoApplication();
            app.start();

        } catch (Exception e) {
            logger.error("Failed to start application", e);
            System.exit(1);
        }
    }

    /**
     * Starts the application.
     */
    public void start() throws IOException {
        // Initialize Repositories
        TaskRepository taskRepository = new InMemoryTaskRepository();
        CategoryRepository categoryRepository = new InMemoryCategoryRepository();

        logger.info("Repositories initialized");

        // Initialize Services
        TaskService taskService = new TaskServiceImpl(taskRepository, categoryRepository);
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository);

        logger.info("Services initialized");

        // Initialize Controllers
        TaskController taskController = new TaskController(taskService);
        CategoryController categoryController = new CategoryController(categoryService);

        logger.info("Controllers initialized");

        // Create and configure HTTP Server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Register API endpoints
        server.createContext("/api/v1/tasks", taskController);
        server.createContext("/api/v1/categories", categoryController);

        // Serve static files (frontend)
        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();

            // Default to index.html
            if (path.equals("/") || path.equals("/index.html")) {
                serveStaticFile(exchange, "src/main/resources/static/index.html", "text/html");
            } else if (path.endsWith(".css")) {
                serveStaticFile(exchange, "src/main/resources/static" + path, "text/css");
            } else if (path.endsWith(".js")) {
                serveStaticFile(exchange, "src/main/resources/static" + path, "application/javascript");
            } else {
                // Try to serve the requested file
                serveStaticFile(exchange, "src/main/resources/static" + path, "text/html");
            }
        });

        server.setExecutor(null); // Use default executor

        server.start();

        logger.info("╔═══════════════════════════════════════════════════════════════╗");
        logger.info("║                                                               ║");
        logger.info("║           ToDo Application Started Successfully!             ║");
        logger.info("║                                                               ║");
        logger.info("║   Frontend:  http://localhost:{}                           ║", PORT);
        logger.info("║   API:       http://localhost:{}/api/v1/tasks              ║", PORT);
        logger.info("║                                                               ║");
        logger.info("║   Press Ctrl+C to stop the server                            ║");
        logger.info("║                                                               ║");
        logger.info("╚═══════════════════════════════════════════════════════════════╝");

        System.out.println("\n✅ Server is running on http://localhost:" + PORT);
        System.out.println("📱 Open your browser and navigate to: http://localhost:" + PORT);
        System.out.println("\nPress Ctrl+C to stop the server\n");

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down ToDo Application...");
            server.stop(0);
            logger.info("Server stopped");
        }));
    }

    /**
     * Serves a static file from the file system.
     *
     * @param exchange    The HTTP exchange
     * @param filePath    Path to the file
     * @param contentType Content type of the file
     */
    private void serveStaticFile(com.sun.net.httpserver.HttpExchange exchange, String filePath, String contentType) {
        try {
            Path path = Paths.get(filePath);

            if (Files.exists(path)) {
                byte[] bytes = Files.readAllBytes(path);
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                // File not found
                String notFound = "<h1>404 - File Not Found</h1>";
                exchange.sendResponseHeaders(404, notFound.length());
                OutputStream os = exchange.getResponseBody();
                os.write(notFound.getBytes());
                os.close();
            }
        } catch (IOException e) {
            logger.error("Error serving static file: {}", filePath, e);
            try {
                exchange.sendResponseHeaders(500, 0);
                exchange.close();
            } catch (IOException ex) {
                logger.error("Error sending error response", ex);
            }
        }
    }
}
