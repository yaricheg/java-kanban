package http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpEpics extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpEpics(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        String method = httpExchange.getRequestMethod();
        Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
        Boolean subtasksTrue = getSubTasksFromPath(httpExchange.getRequestURI().getPath());
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
                taskManager.updateEpic(new Epic(id, name, status, description));
                sendText(httpExchange, "Эпик обновлен", 201);
            } catch (NotFoundException e) {
                taskManager.createEpic(new Epic(name, status, description, startTime, Duration.ofMinutes(duration)));
                sendText(httpExchange, "Эпик добавлен", 201);
            } catch (ValidationException e) {
                response = sendHasInteractions("Эпик");
                sendText(httpExchange, response, 406);
            } catch (IOException e) {
                sendText(httpExchange, "Произошла ошибка при обработке запроса", 500);
            }
        } else if ((method.equals("GET"))) {
            try {
                if (id == null) {
                    response = HttpTaskServer.getJson().toJson(taskManager.getEpics());
                    sendText(httpExchange, response, 200);
                }
                if ((id != null) & (subtasksTrue == false)) {
                    response = HttpTaskServer.getJson().toJson(taskManager.getEpicById(id));
                    sendText(httpExchange, response, 200);
                }
                if ((id != null) & (subtasksTrue == true)) {
                    response = HttpTaskServer.getJson().toJson(taskManager.getEpicSubtasks(id));
                    sendText(httpExchange, response, 200);
                }
            } catch (NotFoundException e) {
                response = sendNotFound("Эпика");
                sendText(httpExchange, response, 404);
            }
        } else if (method.equals("DELETE")) {
            taskManager.deleteEpic(id);
            response = HttpTaskServer.getJson().toJson(taskManager.getEpics());
            sendText(httpExchange, response, 200);
        } else {
            response = "Некорректный метод";
            sendText(httpExchange, response, 200);
        }
    }

    private Boolean getSubTasksFromPath(String path) {
        String[] parts = path.split("/");
        if (parts.length > 3) {
            return true;
        }
        return false;
    }
}

