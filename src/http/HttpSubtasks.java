package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.ValidationException;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpSubtasks extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpSubtasks(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Gson gson = HttpTaskServer.getGson();
        String response;
        String method = httpExchange.getRequestMethod();
        Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
        String json = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        SubTask subTask = gson.fromJson(json, SubTask.class);
        if (method.equals("POST")) {
            if (id != null) {
                try {
                    taskManager.updateSubTask(new SubTask(id, subTask.getName(), subTask.getStatus().toString(),
                            subTask.getDescription(), subTask.getEpic(),
                            subTask.getStartTime(), subTask.getDuration()));

                    sendText(httpExchange, "Подзадача обновлена", 201);
                } catch (NotFoundException e) {
                    sendNotFound(httpExchange, "Подзадача не найдена");
                } catch (ValidationException e) {
                    sendHasInteractions(httpExchange, "Подзадача пересекается с другими задачами");
                } catch (IOException e) {
                    sendText(httpExchange, "Произошла ошибка при обработке запроса", 500);
                }
            }
            if (id == null) {
                try {
                    taskManager.createSubTask(new SubTask(subTask.getName(), subTask.getStatus().toString(),
                            subTask.getDescription(), subTask.getEpic(),
                            subTask.getStartTime(), subTask.getDuration()));
                    sendText(httpExchange, "Подзадача добавлена", 201);
                } catch (ValidationException e) {
                    sendHasInteractions(httpExchange, "Подзадача пересекается с другими задачами");
                }
            }
        } else if (method.equals("GET")) {
            if (id == null) {
                response = HttpTaskServer.getGson().toJson(taskManager.getSubTasks());
                sendText(httpExchange, response, 201);
            }
            if (!(id == null)) {
                try {
                    response = HttpTaskServer.getGson().toJson(taskManager.getSubTaskById(id));
                    sendText(httpExchange, response, 200);
                } catch (NotFoundException e) {
                    sendNotFound(httpExchange, "Подзадача не найдена");
                }
            }
        } else if (method.equals("DELETE")) {
            taskManager.deleteSubTask(id);
            response = "Подзадача удалена";
            sendText(httpExchange, response, 200);
        } else {
            response = "METHOD_NOT_ALLOWED";
            sendText(httpExchange, response, 409);
        }
    }
}

