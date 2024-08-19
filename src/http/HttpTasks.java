package http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.ValidationException;
import model.Status;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public class HttpTasks extends BaseHttpHandler {

    private final TaskManager taskManager;

    public HttpTasks(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        String method = httpExchange.getRequestMethod();
        Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
        if (method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) { // проверяем, является ли элемент JSON-объектом
                throw new NotFoundException("Неправильный формат Json");
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            Status status = Status.valueOf(jsonObject.get("status").getAsString());
            String description = jsonObject.get("description").getAsString();
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            Integer duration = jsonObject.get("duration").getAsInt();
            try {
                taskManager.updateTask(new Task(id, name, status, description,
                        startTime, Duration.ofMinutes(duration)));
                sendText(httpExchange, "Задача обновлена", 201);
            } catch (NotFoundException e) {
                taskManager.createTask(new Task(name, status, description,
                        startTime, Duration.ofMinutes(duration)));
                sendText(httpExchange, "Задача добавлена", 201);
            } catch (ValidationException e) {
                response = sendHasInteractions("Задача");
                sendText(httpExchange, response, 406);
            } catch (IOException e) {
                sendText(httpExchange, "Произошла ошибка при обработке запроса", 500);
            }
        } else if (method.equals("GET")) {
            if (id == null) {
                List<Task> tasks = taskManager.getTasks();
                response = HttpTaskServer.getJson().toJson(tasks);
                sendText(httpExchange, response, 200);
            } else {
                try {
                    response = HttpTaskServer.getJson().toJson(taskManager.getTaskById(id));
                    sendText(httpExchange, response, 200);
                } catch (NotFoundException e) {
                    response = sendNotFound("Задачи");
                    sendText(httpExchange, response, 404);
                }
            }
        } else if (method.equals("DELETE")) {
            taskManager.deleteTask(id);
            response = HttpTaskServer.getJson().toJson(taskManager.getTasks());
            sendText(httpExchange, response, 200);
        } else {
            response = "Некорректный метод";
            sendText(httpExchange, response, 200);
        }
    }
}



