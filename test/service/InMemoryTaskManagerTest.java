package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager memoryTaskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;

// По заданию
    @BeforeEach
    void init() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        memoryTaskManager = new InMemoryTaskManager(historyManager);
        task = memoryTaskManager.createTask(new Task("Новая задача", Status.NEW, "Задача 1"));
        epic = memoryTaskManager.createEpic(new Epic("Новый Эпик", "Задача 1"));
        subTask = memoryTaskManager.createSubTask(new SubTask("Новая подзадача", Status.NEW, "подзадача 1", epic.getId()));
    }



    private static void assertEqualsInMemoryTaskManager(InMemoryTaskManager expected, InMemoryTaskManager actual, String message) {
        assertEquals(expected.getAllTasks(), actual.getAllTasks(), message + ", tasks");
        assertEquals(expected.getAllEpics(), actual.getAllEpics(), message + ", tasks");
        assertEquals(expected.getAllSubTasks(), actual.getAllSubTasks(), message + ", tasks");
    }

    @DisplayName("Проверяется,что утилитарный класс всегда возвращает готовые к работе экземпляры менеджеров")
    @Test
    void taskManagers() {
        Managers managers = new Managers();
        InMemoryTaskManager inMemoryTaskManager =  managers.getDefaults();
        inMemoryTaskManager.createTask(new Task("Новая задача", Status.NEW, "Задача 1"));
        Epic epic = inMemoryTaskManager.createEpic(new Epic("Новый Эпик", "Задача 1"));
        inMemoryTaskManager.createSubTask(new SubTask("Новая подзадача", Status.NEW, "подзадача 1", epic.getId()));
        assertEqualsInMemoryTaskManager(inMemoryTaskManager, memoryTaskManager, "менеджеры должны совпадать");
    }



    @DisplayName("Проверяется, что InMemoryTaskManager добавляет задачи разного типа и может найти их по id")
    @Test
    void ShouldCreateTaskAndSearchId(){
        assertEquals(task, memoryTaskManager.tasks.get(task.getId()));
        assertEquals(epic, memoryTaskManager.epics.get(epic.getId()));
        assertEquals(subTask, memoryTaskManager.subtasks.get(subTask.getId()));

    }

    @DisplayName("Проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    @Test
    void ShouldBeTrueTasks(){
        assertEquals(task, memoryTaskManager.tasks.get(task.getId()));
        assertEquals(epic, memoryTaskManager.epics.get(epic.getId()));
        assertEquals(subTask, memoryTaskManager.subtasks.get(subTask.getId()));

    }

    @DisplayName("Проверяется, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    @Test
    void ShouldBeFalseIdTasks(){
        Task task1 = memoryTaskManager.createTask(new Task(1,"Простая задача", Status.NEW, "2+2"));
        boolean check =  task1.getId() == task.getId();
        assertFalse(check);
    }

    @DisplayName("HistoryManager должен сохранять предыдущую версию задачи и ее данных")
    @Test
    void ShouldCheckTasksInHistoryManager(){
        Task task1 = memoryTaskManager.tasks.get(task.getId());
        memoryTaskManager.getTask(1);
        memoryTaskManager.updateTask(new Task(1,"Старая задача", Status.DONE, "Задача 1 выполнена"));
        Task updateTask1 = memoryTaskManager.tasks.get(task.getId());
        memoryTaskManager.getTask(1);
        assertEquals(2,memoryTaskManager.getHistory().size(),"Значения должны совпадать");
        assertEquals(task1, memoryTaskManager.getHistory().get(0),"Задачи должны совпадать");
        assertEquals(updateTask1, memoryTaskManager.getHistory().get(1),"Задачи должны совпадать");
    }

    //Проверки не входящие в ТЗ 5

    @DisplayName("Проверка удаления задачи из менеджера")
    @Test
    void ShouldBe0DeleteTaskFromManager(){
        memoryTaskManager.deleteTask(1);
        assertEquals(0,memoryTaskManager.tasks.size());
    }

    @DisplayName("Проверка удаления Эпика из менеджера")
    @Test
    void ShouldBe0DeleteEpicFromManager(){
        memoryTaskManager.deleteEpic(2);
        assertEquals(0,memoryTaskManager.epics.size());
    }
    @DisplayName("Проверка удаления Подзадачи из менеджера")
    @Test
    void ShouldBe0DeleteSubTaskFromManager(){
        memoryTaskManager.deleteSubTask(3);
        assertEquals(0,memoryTaskManager.subtasks.size());
    }

    @DisplayName("Проверка обновления задачи в менеджере")
    @Test
    void ShouldBeFalseUpdateTask(){
        memoryTaskManager.updateTask(new Task(1,"Старая задача",Status.DONE, "Выполнена"));
        boolean check = task.equals(memoryTaskManager.tasks.get(1));
        assertFalse(check);
    }

    public boolean equalsEpic(Epic epic1) {
        return epic1.getId() == epic.getId() && Objects.equals(epic1.getName(),
                epic.getName()) && Objects.equals(epic1.getStatus(),
                epic1.getStatus()) && Objects.equals(epic1.getDescription(), epic);
    }

    @DisplayName("Проверка обновления эпика в менеджере")
    @Test
    void ShouldBeFalseUpdateEpic(){
        memoryTaskManager.updateEpic(new Epic(2,"Старая задача", Status.NEW, "Выполнена"));
        boolean check = equalsEpic(memoryTaskManager.epics.get(2));
        assertFalse(check);
    }
}
