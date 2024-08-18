package http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
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
        System.out.println(method);
        Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
        System.out.println(id);
        Boolean subtasksTrue = getSubTasksFromPath(httpExchange.getRequestURI().getPath());
        System.out.println(subtasksTrue);
        if (method.equals("POST")) {
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Тело запроса:\n" + body);
            JsonElement jsonElement = JsonParser.parseString(body);
            if (!jsonElement.isJsonObject()) { // проверяем, является ли элемент JSON-объектом
                throw new NotFoundException("Неправильный формат Json");
            }
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            System.out.println("Действие 1");
            Status status = Status.valueOf(jsonObject.get("status").getAsString());
            System.out.println("Действие 2");
            String description = jsonObject.get("description").getAsString();
            System.out.println("Действие 3");
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            System.out.println("Действие 4");// localDateTime
            Integer duration = jsonObject.get("duration").getAsInt();
            //int idEpic = jsonObject.get("idEpic").getAsInt();
            System.out.println("После десериализации:" + name + " " + status + " " + description + " ");
            try {
                taskManager.updateEpic(new Epic(id, name, status, description));
                System.out.println("Действие try 1 выполнено");
                response = HttpTaskServer.getJson().toJson(taskManager.getEpicById(id));
                sendText(httpExchange, response, 201);
                System.out.println("Действие try выполнено");
            } catch (NotFoundException e) {
                taskManager.createEpic(new Epic(name, status, description, startTime, Duration.ofMinutes(duration)));
                System.out.println("Выполнено 1");
                //System.out.println(taskManager.getTaskById(id));
                response = HttpTaskServer.getJson().toJson(taskManager.getEpicById(id));
                System.out.println("Выполнено 2");
                sendText(httpExchange, response, 201);
                System.out.println("Действие catch выполнено");

            }
        } else if ((method.equals("GET"))) {
            if (id == null) {
                response = HttpTaskServer.getJson().toJson(taskManager.getEpics());
                sendText(httpExchange, response, 200);
            }
            if ((id != null) & (subtasksTrue == false)) {
                response = HttpTaskServer.getJson().toJson(taskManager.getEpicById(id));
                sendText(httpExchange, response, 200);
            }
            if ((id != null) & (subtasksTrue == true)) {
                response = HttpTaskServer.getJson().toJson(taskManager.getEpicById(id));
                sendText(httpExchange, response, 200);
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

