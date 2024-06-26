import model.Epic;
import model.SubTask;
import model.Task;
import service.FileBackedTaskManager;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static model.Status.DONE;
import static model.Status.NEW;


public class Main {

    private static HistoryManager InMemoryHistoryManager;

    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\yaroslav\\java-kanban\\resources\\task1.txt");
        File file = path.toFile();
        try {
            Files.readString(Path.of("C:\\Users\\yaroslav\\java-kanban\\resources\\task1.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Не смог прочитать: resources/task1.txt", e);
        }

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaultsFile(file);
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        taskManager.createTask(new Task("Сходить в магазин", NEW, "Купить помидоры и огурцы"));

        Epic project = taskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", project.getId()));
        taskManager.createSubTask(new SubTask("Выучить слова", DONE, "задача выполнена вчера", project.getId()));

        Epic travel = taskManager.createEpic(new Epic("Поездка в Грузию", "В Тбилиси"));
        taskManager.createSubTask(new SubTask("Купить билет на самолет", NEW, "По низкой цене", travel.getId()));


        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println();

        taskManager.updateEpic(new Epic(6, "Поездка в Узбекистан", NEW, "в Ташкент"));
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
        System.out.println();


        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubTask(4);
        taskManager.getSubTask(5);
        taskManager.getEpic(6);
        taskManager.getSubTask(7);
        taskManager.getTask(1);

        System.out.println(taskManager.getHistory());
        System.out.println();

        //taskManager.deleteAllEpics();

        FileBackedTaskManager fileBackedTaskManager;
        fileBackedTaskManager = new FileBackedTaskManager(file).loadFromFile(file);

        System.out.println(fileBackedTaskManager.getAllEpics());
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println(fileBackedTaskManager.getAllSubTasks());
    }
}

