package http;

import com.google.gson.Gson;
import model.Epic;
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

public class HttpEpicsTest {

    HistoryManager historyManager = new InMemoryHistoryManager();// создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager(historyManager);
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpEpicsTest() throws IOException {
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
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("EpicTest 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String taskJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> tasksFromManager = manager.getEpics();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("EpicTest 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        //Создание задачи
        Epic epic = new Epic("EpicTest 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String EpicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(EpicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Обновление задачи
        Task epicUpdate = new Task(1, "EpicTest 2 обновленный", "DONE", "Testing epic 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJsonUpdate = gson.toJson(epicUpdate);
        URI url2 = URI.create("http://localhost:8080/epics/1");
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJsonUpdate)).build();
        HttpResponse<String> responseUpdate = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
        // Проверка
        assertEquals(201, responseUpdate.statusCode());
        List<Epic> tasksFromManager = manager.getEpics();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("EpicTest 2 обновленный", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "NEW", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/epics/1");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());

        List<Task> tasksFromManager = manager.getTasks();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testEpic404() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");

        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, responseCreate.statusCode());
        List<Epic> tasksFromManager = manager.getEpics();
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        // Создание первой задачи
        HttpClient client = HttpClient.newHttpClient();
        Epic epic = new Epic(1, "Epic Test 2", "NEW", "Testing epic 2",
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        // вызов метода get
        URI url1 = URI.create("http://localhost:8080/epics/1");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url1).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = HttpTaskServer.getGson().fromJson(responseGet.body().toString(), Epic.class);

        // Проверка
        assertEquals(epic, epic1, "Задачи не совпадают");
    }

}
