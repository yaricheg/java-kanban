package http;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.ValidationException;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
            String json = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = HttpTaskServer.getGson().fromJson(json, Task.class);
            if (id != null) {
                try {
                    taskManager.updateTask(new Task(id, task.getName(), task.getStatus().toString(),
                            task.getDescription(), task.getStartTime(), task.getDuration()));
                    sendText(httpExchange, "Задача обновлена", 201);
                } catch (ValidationException e) {
                    sendHasInteractions(httpExchange, "Задача пересекается с другими задачами");
                } catch (NotFoundException e) {
                    sendNotFound(httpExchange, "Задача не найдена");
                } catch (IOException e) {
                    sendText(httpExchange, "Произошла ошибка при обработке запроса", 500);
                }
            }
            if (id == null) {
                try {
                    taskManager.createTask(new Task(task.getName(), task.getStatus().toString(),
                            task.getDescription(), task.getStartTime(), task.getDuration()));
                    sendText(httpExchange, "Задача добавлена", 201);
                } catch (ValidationException e) {
                    sendHasInteractions(httpExchange, "Задача пересекается с другими задачами");
                }
            }

        } else if (method.equals("GET")) {
            if (id == null) {
                List<Task> tasks = taskManager.getTasks();
                response = HttpTaskServer.getGson().toJson(tasks);
                sendText(httpExchange, response, 200);
            }
            if (!(id == null)) {
                try {
                    response = HttpTaskServer.getGson().toJson(taskManager.getTaskById(id));
                    sendText(httpExchange, response, 200);
                } catch (NotFoundException e) {
                    sendNotFound(httpExchange, "Задача не найдена");
                }
            }
        } else if (method.equals("DELETE")) {
            taskManager.deleteTask(id);
            response = "Задача удалена";
            sendText(httpExchange, response, 200);
        } else {
            response = "METHOD_NOT_ALLOWED";
            sendText(httpExchange, response, 409);
        }
    }
}



