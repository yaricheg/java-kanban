package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TaskManager memoryTaskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;

    // По заданию
    @BeforeEach
    void init() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        memoryTaskManager = Managers.getDefaults();
        task = memoryTaskManager.createTask(new Task("Новая задача", Status.NEW, "Задача 1"));
        epic = memoryTaskManager.createEpic(new Epic("Новый Эпик", "Задача 1"));
        subTask = memoryTaskManager.createSubTask(new SubTask("Новая подзадача", Status.NEW, "подзадача 1", epic.getId()));
    }


    @DisplayName("Проверяется, что InMemoryTaskManager добавляет задачи разного типа и может найти их по id")
    @Test
    void shouldCreateTaskAndSearchId() {
        assertEquals(task, memoryTaskManager.getTask(task.getId()));
        assertEquals(epic, memoryTaskManager.getEpic(epic.getId()));
        assertEquals(subTask, memoryTaskManager.getSubTask(subTask.getId()));

    }

    @DisplayName("Проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    @Test
    void shouldBeTrueTasks() {
        assertEquals(task, memoryTaskManager.getTask(task.getId()));
        assertEquals(epic, memoryTaskManager.getEpic(epic.getId()));
        assertEquals(subTask, memoryTaskManager.getSubTask(subTask.getId()));

    }

    @DisplayName("Проверяется, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    @Test
    void shouldBeFalseIdTasks() {
        Task task1 = memoryTaskManager.createTask(new Task(1, "Простая задача", Status.NEW, "2+2"));
        boolean check = task1.getId() == task.getId();
        assertFalse(check);
    }

    @DisplayName("HistoryManager должен сохранять последнюю версию задачи и ее данных")
    @Test
    void shouldCheckTasksInHistoryManager() {
        memoryTaskManager.updateTask(new Task(1, "Старая задача", Status.DONE, "Задача 1 выполнена"));
        Task task1 = memoryTaskManager.getTask(1);
        assertEquals(memoryTaskManager.getAllTasks(), memoryTaskManager.getHistory(), "Значения должны совпадать");
        //assertEquals(task1, updateTask1,"Задачи должны совпадать");
    }


    @DisplayName("Проверка удаления задачи из менеджера")
    @Test
    void shouldBe0DeleteTaskFromManager() {
        memoryTaskManager.deleteTask(1);
        assertEquals(0, memoryTaskManager.getAllTasks().size());
    }

    @DisplayName("Проверка удаления Эпика из менеджера")
    @Test
    void shouldBe0DeleteEpicFromManager() {
        memoryTaskManager.deleteEpic(2);
        assertEquals(0, memoryTaskManager.getAllEpics().size());
    }

    @DisplayName("Проверка удаления Подзадачи из менеджера")
    @Test
    void shouldBe0DeleteSubTaskFromManager() {
        memoryTaskManager.deleteSubTask(3);
        assertEquals(0, memoryTaskManager.getAllSubTasks().size());
    }

    @DisplayName("Проверка обновления задачи в менеджере")
    @Test
    void shouldBeFalseUpdateTask() {
        memoryTaskManager.updateTask(new Task(1, "Старая задача", Status.DONE, "Выполнена"));
        boolean check = task.equals(memoryTaskManager.getTask(1));
        assertFalse(check);
    }

    public boolean equalsEpic(Epic epic1) {
        return epic1.equals(epic);
    }

    @DisplayName("Проверка обновления эпика в менеджере")
    @Test
    void shouldBeTrueUpdateEpic() {
        memoryTaskManager.updateEpic(new Epic(2, "Старая задача", Status.NEW, "Выполнена"));
        boolean check = equalsEpic(memoryTaskManager.getEpic(2));
        assertTrue(check);
    }
}
