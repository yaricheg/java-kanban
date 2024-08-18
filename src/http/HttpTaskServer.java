package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import model.Status;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        this.httpServer.createContext("/tasks", new HttpTasks(taskManager));
        this.httpServer.createContext("/subtasks", new HttpSubtasks(taskManager));
        this.httpServer.createContext("/epics", new HttpEpics(taskManager));
        this.httpServer.createContext("/history", new HttpHistory(taskManager));
        this.httpServer.createContext("/prioritized", new HttpPrioritized(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefaults();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static Gson getJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
        DurationAdapter durationAdapter = new DurationAdapter();
        StatusAdapter statusAdapter = new StatusAdapter();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter);
        gsonBuilder.registerTypeAdapter(Duration.class, durationAdapter);
        gsonBuilder.registerTypeAdapter(Status.class, statusAdapter);
        return gsonBuilder.create();
    }
}

