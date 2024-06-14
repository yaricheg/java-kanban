package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {

    @Test
    void loadData() throws IOException {
        File file = File.createTempFile("text", ".txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        fileBackedTaskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        Epic project = fileBackedTaskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        fileBackedTaskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", project.getId()));
        FileBackedTaskManager fileBackedTaskManager1 = fileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.getAllTasks(), fileBackedTaskManager1.getAllTasks());
        assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManager1.getAllEpics());
        assertEquals(fileBackedTaskManager.getAllSubTasks(), fileBackedTaskManager1.getAllSubTasks());

    }
}