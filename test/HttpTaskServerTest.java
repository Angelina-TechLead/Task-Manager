import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {

    private HttpTaskServer server;
    private TaskManager taskManager;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        server.start();
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    @Test
    public void testGetTasks() throws IOException {
        URL url = new URL("http://localhost:8080/tasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void testPostTask() throws IOException {
        URL url = new URL("http://localhost:8080/tasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write("{\"name\":\"Test Task\",\"description\":\"Test Description\"}".getBytes());

        int responseCode = connection.getResponseCode();
        assertEquals(201, responseCode);
    }

    @Test
    public void testDeleteTask() throws IOException {
        taskManager.addTask(new Task("Test Task", "Test Description", 1, TaskStatus.NEW, Duration.ZERO, LocalDateTime.now()));

        URL url = new URL("http://localhost:8080/tasks/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(204, responseCode);
    }

    @Test
    public void testGetSubtasks() throws IOException {
        URL url = new URL("http://localhost:8080/subtasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void testPostSubtask() throws IOException {
        URL url = new URL("http://localhost:8080/subtasks");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write("{\"name\":\"Test Subtask\",\"description\":\"Test Description\",\"epicId\":1}".getBytes());

        int responseCode = connection.getResponseCode();
        assertEquals(201, responseCode);
    }

    @Test
    public void testDeleteSubtask() throws IOException {
        taskManager.addSubtask(new Subtask("Test Subtask", "Test Description", 1, TaskStatus.NEW, 1, Duration.ZERO, LocalDateTime.now()));

        URL url = new URL("http://localhost:8080/subtasks/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(204, responseCode);
    }

    @Test
    public void testGetEpics() throws IOException {
        URL url = new URL("http://localhost:8080/epics");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void testPostEpic() throws IOException {
        URL url = new URL("http://localhost:8080/epics");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write("{\"name\":\"Test Epic\",\"description\":\"Test Description\"}".getBytes());

        int responseCode = connection.getResponseCode();
        assertEquals(201, responseCode);
    }

    @Test
    public void testDeleteEpic() throws IOException {
        taskManager.addEpic(new Epic("Test Epic", "Test Description", 1, TaskStatus.NEW));

        URL url = new URL("http://localhost:8080/epics/1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(204, responseCode);
    }

    @Test
    public void testGetHistory() throws IOException {
        URL url = new URL("http://localhost:8080/history");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException {
        URL url = new URL("http://localhost:8080/prioritized");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", 1,
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = taskManager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }
}
