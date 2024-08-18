/*import model.Epic;
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
import java.time.Duration;
import java.time.LocalDateTime;

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
        taskManager.createTask(new Task("Сходить в кино", NEW, "С друзьями в 19:00",
                LocalDateTime.of(2024, 6, 25, 19, 0), Duration.ofMinutes(90)));
        taskManager.createTask(new Task("Сходить в магазин", NEW, "Купить помидоры и огурцы",
                LocalDateTime.of(2024, 6, 25, 12, 0), Duration.ofMinutes(60)));

        Epic project = taskManager.createEpic(new Epic("Сдача проекта", NEW, "По английскому языку",
                LocalDateTime.of(2025, 6, 26, 10, 0), Duration.ofMinutes(2)));
        taskManager.createSubTask(new SubTask("Перевести текст", NEW, "Про кошку", project.getId(),
                LocalDateTime.of(2024, 6, 26, 10, 0), Duration.ofMinutes(120)));
        taskManager.createSubTask(new SubTask("Выучить слова", DONE, "задача выполнена вчера", project.getId(),
                LocalDateTime.of(2024, 6, 26, 13, 0), Duration.ofMinutes(120)));

        Epic travel = taskManager.createEpic(new Epic(6, "Поездка в Грузию", NEW, "В Тбилиси",
                LocalDateTime.of(2026, 6, 26, 10, 0), Duration.ofMinutes(0)));
        taskManager.createSubTask(new SubTask("Купить билет на самолет", NEW, "По низкой цене", travel.getId(),
                LocalDateTime.of(2024, 6, 26, 16, 0), Duration.ofMinutes(120)));


        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
        System.out.println();

        taskManager.updateEpic(new Epic(6, "Поездка в Узбекистан", NEW, "в Ташкент"));
        taskManager.updateTask(new Task(1, "Поездка в Монголию", NEW, "в Улан-Батор",LocalDateTime.of(2030, 6, 26, 10, 0), Duration.ofMinutes(120) ));
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getSubTasks());
        System.out.println();


        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getSubTaskById(4);
        taskManager.getSubTaskById(5);
        taskManager.getEpicById(6);
        taskManager.getSubTaskById(7);
        taskManager.getTaskById(1);

        System.out.println(taskManager.getHistory());
        System.out.println();

        //taskManager.deleteAllEpics();

        FileBackedTaskManager fileBackedTaskManager;
        fileBackedTaskManager = new FileBackedTaskManager(file).loadFromFile(file);

        System.out.println(fileBackedTaskManager.getEpics());
        System.out.println(fileBackedTaskManager.getTasks());
        System.out.println(fileBackedTaskManager.getSubTasks());
        System.out.println();

        System.out.println(taskManager.getPrioritizedTasks());

    }
}*/

