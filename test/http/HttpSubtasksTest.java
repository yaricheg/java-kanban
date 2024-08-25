package http;

import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
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

public class HttpSubtasksTest {

    HistoryManager historyManager = new InMemoryHistoryManager();// создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager(historyManager);
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpSubtasksTest() throws IOException {
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
    public void testAddSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        // создаем эпик
        Epic epic = new Epic("Epic 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));

        // создаём подзадачу
        SubTask subTask = new SubTask("subTask 2", "NEW", "Testing task 2", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));

        String epicJson = gson.toJson(epic);
        String epicSubTask = gson.toJson(subTask);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestCreateEpic = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> responseEpic = client.send(requestCreateEpic, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestSubTask = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(epicSubTask)).build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, responseSubTask.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<SubTask> tasksFromManager = manager.getSubTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("subTask 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        //Создание задачи
        HttpClient client = HttpClient.newHttpClient();
        // создаем эпик
        Epic epic = new Epic("Epic 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));

        // создаём подзадачу
        SubTask subTask = new SubTask("subTask 2", "NEW", "Testing task 2", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));

        String epicJson = gson.toJson(epic);
        String subTaskJson = gson.toJson(subTask);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestCreateEpic = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> responseEpic = client.send(requestCreateEpic, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestSubTask = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());

        // Обновление подзадачи
        Task SubtaskUpdate = new SubTask(2, "subTask 2 end", "DONE", "Testing task 2 end", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJsonUpdate = gson.toJson(SubtaskUpdate);
        URI url2 = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest requestUpdate = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJsonUpdate)).build();
        HttpResponse<String> responseUpdate = client.send(requestUpdate, HttpResponse.BodyHandlers.ofString());
        // Проверка
        assertEquals(201, responseUpdate.statusCode());
        List<SubTask> tasksFromManager = manager.getSubTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("subTask 2 end", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        // создаем эпик
        Epic epic = new Epic("Epic 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));

        // создаём подзадачу
        SubTask subTask = new SubTask("subTask 2", "NEW", "Testing task 2", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));

        String epicJson = gson.toJson(epic);
        String subTaskJson = gson.toJson(subTask);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestCreateEpic = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> responseEpic = client.send(requestCreateEpic, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestSubTask = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(subTaskJson)).build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());

        URI url2 = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest requestDelete = HttpRequest.newBuilder().uri(url2).DELETE().build();
        HttpResponse<String> responseDelete = client.send(requestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());

        List<SubTask> tasksFromManager = manager.getSubTasks();

        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testSubTask404() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");

        HttpRequest requestCreate = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> responseCreate = client.send(requestCreate, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, responseCreate.statusCode());
    }

    @Test
    public void testSubTask406() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        // создаем эпик
        Epic epic = new Epic("Epic 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        // создаём подзадачу
        SubTask subTask = new SubTask("subTask 2", "NEW", "Testing task 2", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));

        String epicJson = gson.toJson(epic);
        String epicSubTask = gson.toJson(subTask);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestCreateEpic = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> responseEpic = client.send(requestCreateEpic, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestSubTask = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(epicSubTask)).build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());

        // Создание пересекающейся задачи
        SubTask taskValidation = new SubTask("subTask 3", "NEW", "Testing task 3", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJsonValidation = gson.toJson(taskValidation);
        URI url2 = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestValidation = HttpRequest.newBuilder().uri(url2).POST(HttpRequest.BodyPublishers.ofString(taskJsonValidation)).build();
        HttpResponse<String> responseValidation = client.send(requestValidation, HttpResponse.BodyHandlers.ofString());

        // Проверка
        assertEquals(406, responseValidation.statusCode());
        List<SubTask> tasksFromManager = manager.getSubTasks();
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        // Создание первой задачи
        HttpClient client = HttpClient.newHttpClient();
        // создаем эпик
        Epic epic = new Epic("Epic 1", "NEW", "Testing epic 1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        // создаём подзадачу
        SubTask subTask = new SubTask(2, "subTask 2", "NEW", "Testing task 2", 1,
                LocalDateTime.now(), Duration.ofMinutes(5));

        String epicJson = gson.toJson(epic);
        String epicSubTask = gson.toJson(subTask);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest requestCreateEpic = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> responseEpic = client.send(requestCreateEpic, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestSubTask = HttpRequest.newBuilder().uri(url1).POST(HttpRequest.BodyPublishers.ofString(epicSubTask)).build();
        HttpResponse<String> responseSubTask = client.send(requestSubTask, HttpResponse.BodyHandlers.ofString());

        // вызов метода get
        URI url2 = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(url2).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = HttpTaskServer.getGson().fromJson(responseGet.body().toString(), SubTask.class);

        // Проверка
        assertEquals(subTask, subTask1, "Задачи не совпадают");
    }
}

