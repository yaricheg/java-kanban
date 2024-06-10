import model.Epic;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static model.Status.DONE;
import static model.Status.NEW;


public class Main {

    public static void main(String[] args) {
        // Path path = Paths.get("C:\\Users\\yaroslav\\java-kanban\\resources\\task.csv");
        // File file = path.toFile();
        try {
            Files.readString(Path.of("C:\\Users\\yaroslav\\java-kanban\\resources\\task.csv"));
        } catch (IOException e) {
            throw new RuntimeException("Не смог прочитать: resources/task.csv ", e);
        }

        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaults();
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        taskManager.createTask(new Task("Сходить в магазин", NEW, "Купить помидоры и огурцы"));

        Epic project = taskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", project.getId()));
        taskManager.createSubTask(new SubTask("Выучить слова", DONE, "задача выполнена вчера", project.getId()));

        Epic travel = taskManager.createEpic(new Epic("Поездка в Грузию", "В Тбилиси"));
        taskManager.createSubTask(new SubTask("Взять вещи", NEW, "одежда, обувь, продукты", travel.getId()));


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

        taskManager.deleteAllEpics();
        System.out.println(taskManager.getHistory());


    }
}

