import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import static model.Status.NEW;
import static model.Status.DONE;
import static model.Status.IN_PROGRESS;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00"));
        taskManager.createTask(new Task("Сходить в магазин", NEW, "Купить помидоры и огурцы"));

        Epic Project  = taskManager.createEpic(new Epic("Сдача проекта", "По английскому языку"));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, ""), Project);
        taskManager.createSubTask(new SubTask("Выучить слова", DONE, "задача выполнена вчера"), Project);


        Epic Travel = taskManager.createEpic(new Epic("Поездка в Грузию", "В Тбилиси"));
        taskManager.createSubTask(new SubTask("Взять вещи", NEW, "одежда, обувь, продукты"), Travel);


        taskManager.getAllTasks();
        taskManager.getAllEpics();
        taskManager.getAllSubTasks();
        System.out.println();

        taskManager.updateEpic(new Epic(6, "Поездка в Узбекистан", NEW, "в Ташкент"));
        taskManager.getAllEpics();
        taskManager.getAllSubTasks();
        System.out.println();

        taskManager.deleteTask(1);
        taskManager.deleteSubTask(7);
        taskManager.deleteEpic(3);



        taskManager.getAllTasks();
        taskManager.getAllEpics();
        taskManager.getAllSubTasks();

    }

}

