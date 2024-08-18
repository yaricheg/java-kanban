package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {

    protected void sendText(HttpExchange h, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(rCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected String sendNotFound(String text) {
        return "Код состояния: 404 " + ". " + text + " нет.";

    }

    protected String sendHasInteractions(String text) {
        return "Код состояния: 406 " + ". " + text + " пересекается с другой задачей/подзадачей";
    }

    protected Integer getIdFromPath(String path) {
        String[] parts = path.split("/");
        if (parts.length >= 3) {
            return Integer.parseInt(parts[2]);
        }
        return null;
    }


}