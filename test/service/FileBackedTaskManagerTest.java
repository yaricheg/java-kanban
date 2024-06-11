package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static model.Status.NEW;

class FileBackedTaskManagerTest {

    @Test
    void saveData() throws IOException {
        File file = File.createTempFile("text", ".txt");
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaultsFile(file);
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        Epic project = taskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", project.getId()));

    }

    @Test
    void loadData() throws IOException {
        File file = File.createTempFile("text", ".txt");
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaultsFile(file);
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        Epic project = taskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", project.getId()));
        taskManager.loadFromFile();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
    }

}