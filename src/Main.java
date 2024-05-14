import model.Epic;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import static model.Status.DONE;
import static model.Status.NEW;


public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();
        TaskManager taskManager = managers.getDefaults();
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        taskManager.createTask(new Task("Сходить в магазин", NEW, "Купить помидоры и огурцы"));

        Epic Project = taskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", Project.getId()));
        taskManager.createSubTask(new SubTask("Выучить слова", DONE, "задача выполнена вчера", Project.getId()));

        Epic Travel = taskManager.createEpic(new Epic("Поездка в Грузию", "В Тбилиси"));
        taskManager.createSubTask(new SubTask("Взять вещи", NEW, "одежда, обувь, продукты", Travel.getId()));


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

        System.out.println(taskManager.getHistory());
    }
}

