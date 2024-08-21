package http;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class HttpPrioritized extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpPrioritized(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET")) {
            response = HttpTaskServer.getGson().toJson(taskManager.getPrioritizedTasks());
            sendText(httpExchange, response, 200);
        } else {
            response = "Некорректный метод";
            sendText(httpExchange, response, 200);
        }
    }
}

