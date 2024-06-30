package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.Status.NEW;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    FileBackedTaskManagerTest() throws IOException {
    }

    private File file = File.createTempFile("text", ".txt");

    @Override
    @BeforeEach
    void init() throws IOException {
        taskManager = new FileBackedTaskManager(file);
        task = taskManager.createTask(new Task("Новая задача", NEW, "Задача 1",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        epic = taskManager.createEpic(new Epic(6, "Новый Эпик", NEW, "Задача 1",
                LocalDateTime.of(2027, 7, 12, 12, 12), Duration.ofMinutes(30)));
        subTask = taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic.getId(),
                LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30)));

    }

    @DisplayName("Проверка, отсортированного списка после восстановления файла")
    @Test
    void CheckSorted() throws IOException {
        //File check = File.createTempFile("tex", ".txt");
        Path path = Paths.get("C:\\Users\\yaroslav\\check1.txt");
        File check = path.toFile();
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubTasks();
        taskManager = new FileBackedTaskManager(check);
        task = taskManager.createTask(new Task("Новая задача", NEW, "Задача 1",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        epic = taskManager.createEpic(new Epic(6, "Новый Эпик", NEW, "Задача 1",
                LocalDateTime.of(2027, 7, 12, 12, 12), Duration.ofMinutes(30)));
        subTask = taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic.getId(),
                LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30)));
        FileBackedTaskManager fileBackedTaskManager = taskManager.loadFromFile(check);
        Assertions.assertEquals(taskManager.getPrioritizedTasks(), fileBackedTaskManager.getPrioritizedTasks());
    }
}