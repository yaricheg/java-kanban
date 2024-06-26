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

class FileBackedTaskManagerTest {
    FileBackedTaskManagerTest() throws IOException {
    }

    FileBackedTaskManager fileBackedTaskManager;
    File file = File.createTempFile("text", ".txt");

    @BeforeEach
    void init() throws IOException {
        fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.createTask(new Task(1, "Сходить в кино", NEW, "С друзьями в 19:00",
                LocalDateTime.of(2024, 6, 15, 12, 0), Duration.ofMinutes(30)));
        Epic project = fileBackedTaskManager.createEpic(new Epic(2, "Сдача проекта", NEW, "По английскому языку",
                LocalDateTime.of(2025, 6, 15, 12, 0), Duration.ofMinutes(30)));
        fileBackedTaskManager.createSubTask(new SubTask(3, "Перевести текст", NEW, "Про кошку", project.getId(),
                LocalDateTime.of(2024, 6, 15, 15, 0), Duration.ofMinutes(30)));

    }

    @Test
    void loadData() throws IOException {
        FileBackedTaskManager fileBackedTaskManager1 = fileBackedTaskManager.loadFromFile(file);
        assertEquals(fileBackedTaskManager.getAllTasks(), fileBackedTaskManager1.getAllTasks());
        assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManager1.getAllEpics());
        assertEquals(fileBackedTaskManager.getAllSubTasks(), fileBackedTaskManager1.getAllSubTasks());
    }

    @Test
    void CheckSorted() throws IOException {


    }
}