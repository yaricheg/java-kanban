package http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.ValidationException;
import model.Status;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpSubtasks extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpSubtasks(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = null;
        String method = httpExchange.getRequestMethod();
        System.out.println(method);
        Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
        System.out.println(id);
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
            int idEpic = jsonObject.get("idEpic").getAsInt();
            System.out.println("После десериализации:" + name + " " + status + " " + description + " "
                    + startTime + " " + Duration.ofMinutes(duration));
            try {
                taskManager.updateSubTask(new SubTask(name, status, description, idEpic,
                        startTime, Duration.ofMinutes(duration)));
                System.out.println("Действие выполнено");
                //response = HttpTaskServer.getJson().toJson(taskManager.getSubTaskById(id));
                sendText(httpExchange, "Подзадача добавлена", 201);
                System.out.println("Действие try выполнено");
            } catch (NotFoundException e) {
                taskManager.createSubTask(new SubTask(name, status, description, idEpic,
                        startTime, Duration.ofMinutes(duration)));
                System.out.println("Выполнено 1");
                //response = HttpTaskServer.getJson().toJson(taskManager.getSubTaskById(subTask.getId()));
                System.out.println("Выполнено 2");
                sendText(httpExchange, "Подзадача добавлена", 201);
                System.out.println("Действие catch выполнено");
            } catch (ValidationException e) {
                response = sendHasInteractions("Подзадача");
                sendText(httpExchange, response, 406);

            }
        } else if (method.equals("GET")) {
            if (id == null) {
                response = HttpTaskServer.getJson().toJson(taskManager.getSubTasks());
            } else {
                try {
                    response = HttpTaskServer.getJson().toJson(taskManager.getSubTaskById(id));
                    sendText(httpExchange, response, 200);
                } catch (NotFoundException e) {
                    response = sendNotFound("Подзадача");
                    sendText(httpExchange, response, 404);
                }
            }
            sendText(httpExchange, response, 200);
        } else if (method.equals("DELETE")) {
            taskManager.deleteTask(id);
            response = HttpTaskServer.getJson().toJson(taskManager.getSubTasks());
            sendText(httpExchange, response, 200);
        } else {
            response = "Некорректный метод";
            sendText(httpExchange, response, 200);
        }
    }
}

