package http;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class HttpHistory extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HttpHistory(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        String method = httpExchange.getRequestMethod();
        response = HttpTaskServer.getJson().toJson(taskManager.getHistory());
        sendText(httpExchange, response, 200);
    }
}

