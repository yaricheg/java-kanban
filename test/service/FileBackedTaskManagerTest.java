package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    FileBackedTaskManagerTest() throws IOException {
    }

    File file = File.createTempFile("text", ".txt");

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

    @Test
    void CheckSorted() throws IOException {
        FileBackedTaskManager fileBackedTaskManager1 = taskManager.loadFromFile(file);
        assertEquals(taskManager.getPrioritizedTasks(), fileBackedTaskManager1.getPrioritizedTasks());
    }
}