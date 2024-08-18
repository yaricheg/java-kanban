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
            // Integer idJson = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            System.out.println("Действие 1");
            Status status = Status.valueOf(jsonObject.get("status").getAsString());
            System.out.println("Действие 2");
            String description = jsonObject.get("description").getAsString();
            System.out.println("Действие 3");
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            System.out.println("Действие 4");// localDateTime
            Integer duration = jsonObject.get("duration").getAsInt();
            //LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("endTime").getAsString());
            System.out.println("После десериализации:" + name + " " + status + " " + description + " "
                    + startTime + " " + Duration.ofMinutes(duration));


            try {
                System.out.println("Выполнено try 1");
                taskManager.updateTask(new Task(id, name, status, description,
                        startTime, Duration.ofMinutes(duration)));
                System.out.println("Действие выполнено");
                response = HttpTaskServer.getJson().toJson(taskManager.getTaskById(id));
                sendText(httpExchange, response, 201);
                System.out.println("Действие try выполнено");
            } catch (NotFoundException e) {
                taskManager.createTask(new Task(name, status, description,
                        startTime, Duration.ofMinutes(duration)));
                System.out.println("Выполнено 1");
                response = HttpTaskServer.getJson().toJson(taskManager.getTaskById(id));
                System.out.println("Выполнено 2");
                sendText(httpExchange, response, 201);
                System.out.println("Действие catch выполнено");
            } catch (ValidationException e) {
                response = sendHasInteractions("Задача");
                sendText(httpExchange, response, 404);

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
                    response = sendNotFound("Задача");
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



