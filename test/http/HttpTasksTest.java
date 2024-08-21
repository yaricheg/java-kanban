package http;

import com.google.gson.Gson;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTasksTest {

    HistoryManager historyManager = new InMemoryHistoryManager();// создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager(historyManager);
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        //Создание задачи
        HttpClient client = HttpClient.newHttpClient();
        Task taskCreate = new Task("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJsonCreate = gson.toJson(taskCreate);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJsonCreate)).build();
        HttpResponse<String> response = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        // Обновление задачи
        Task taskUpdate = new Task(1, "Test 2 обновленный", "DONE", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJsonUpdate = gson.toJson(taskUpdate);
        URI url2 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJsonUpdate)).build();
        HttpResponse<String> responseUpdate = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
        // Проверка
        assertEquals(201, responseUpdate.statusCode());
        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2 обновленный", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testTask404() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, responseCreate.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testTask406() throws IOException, InterruptedException {
        // Создание первой задачи
        HttpClient client = HttpClient.newHttpClient();
        Task task = new Task("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        // Создание пересекающейся задачи
        HttpClient client2 = HttpClient.newHttpClient();
        Task taskValidation = new Task("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJsonValidation = gson.toJson(taskValidation);
        URI url2 = URI.create("http://localhost:8080/tasks");
        HttpRequest requestValidation = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJsonValidation)).build();
        HttpResponse<String> responseValidation = client2.send(requestValidation, HttpResponse.BodyHandlers.ofString());

        // Проверка
        assertEquals(406, responseValidation.statusCode());
        List<Task> tasksFromManager = manager.getTasks();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        // Создание первой задачи
        HttpClient client = HttpClient.newHttpClient();
        Task task = new Task(1, "Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        // вызов метода get
        URI url1 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Task task1 = HttpTaskServer.getGson().fromJson(responseGet.body().toString(), Task.class);

        // Проверка
        assertEquals(task, task1, "Задачи не совпадают");
    }

}


