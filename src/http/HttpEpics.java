package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpEpics extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpEpics(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Gson gson = HttpTaskServer.getGson();
        String response;
        String method = httpExchange.getRequestMethod();
        Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
        Boolean subtasksTrue = getSubTasksFromPath(httpExchange.getRequestURI().getPath());
        String json = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(json, Epic.class);
        if (method.equals("POST")) {
            if (id != null) {
                try {
                    taskManager.updateEpic(new Epic(id, epic.getName(),
                            epic.getStatus().toString(), epic.getDescription(),
                            epic.getStartTime(), epic.getDuration()));
                    sendText(httpExchange, "Эпик обновлен", 201);
                } catch (NotFoundException e) {
                    sendNotFound(httpExchange, "Эпик не найден");
                } catch (ValidationException e) {
                    sendHasInteractions(httpExchange, "Эпик пересекается с другими задачами");
                } catch (IOException e) {
                    sendText(httpExchange, "Произошла ошибка при обработке запроса", 500);
                }
            }
            if (id == null) {
                taskManager.createEpic(new Epic(epic.getName(),
                        epic.getStatus().toString(), epic.getDescription(),
                        epic.getStartTime(), epic.getDuration()));
                sendText(httpExchange, "Эпик добавлен", 201);

            }
        } else if (method.equals("GET")) {
            try {
                if (id == null) {
                    response = HttpTaskServer.getGson().toJson(taskManager.getEpics());
                    sendText(httpExchange, response, 200);
                }
                if ((id != null) & (subtasksTrue == false)) {
                    response = HttpTaskServer.getGson().toJson(taskManager.getEpicById(id));
                    sendText(httpExchange, response, 200);
                }
                if ((id != null) & (subtasksTrue == true)) {
                    response = HttpTaskServer.getGson().toJson(taskManager.getEpicSubtasks(id));
                    sendText(httpExchange, response, 200);
                }
            } catch (NotFoundException e) {
                sendNotFound(httpExchange, "Эпик не найден");
            }
        } else if (method.equals("DELETE")) {
            taskManager.deleteEpic(id);
            response = "Эпик удален";
            sendText(httpExchange, response, 200);
        } else {
            response = " METHOD_NOT_ALLOWED";
            sendText(httpExchange, response, 409);
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

