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
        response = HttpTaskServer.getJson().toJson(taskManager.getPrioritizedTasks());
        sendText(httpExchange, response, 200);

    }
}

