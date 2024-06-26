package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static model.Status.*;
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
        task = memoryTaskManager.createTask(new Task("Новая задача", NEW, "Задача 1", LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        epic = memoryTaskManager.createEpic(new Epic(6, "Новый Эпик", NEW, "Задача 1"));
        subTask = memoryTaskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic.getId(),
                LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30)));
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
        Task task1 = memoryTaskManager.createTask(new Task(1, "Простая задача", NEW, "2+2",
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        boolean check = task1.getId() == task.getId();
        assertFalse(check);
    }

    @DisplayName("HistoryManager должен сохранять последнюю версию задачи и ее данных")
    @Test
    void shouldCheckTasksInHistoryManager() {
        memoryTaskManager.updateTask(new Task(1, "Старая задача", DONE, "Задача 1 выполнена",
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        Task task1 = memoryTaskManager.getTask(1);
        assertEquals(memoryTaskManager.getAllTasks(), memoryTaskManager.getHistory(), "Значения должны совпадать");

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
        memoryTaskManager.updateTask(new Task(1, "Старая задача", DONE, "Выполнена", LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        boolean check = task.equals(memoryTaskManager.getTask(1));
        assertFalse(check);
    }

    public boolean equalsEpic(Epic epic1) {
        return epic1.equals(epic);
    }

    @DisplayName("Проверка обновления эпика в менеджере")
    @Test
    void shouldBeTrueUpdateEpic() {
        memoryTaskManager.updateEpic(new Epic(2, "Старая задача", NEW, "Выполнена"));
        boolean check = equalsEpic(memoryTaskManager.getEpic(2));
        assertTrue(check);
    }

    // Добавлено в 8 проект
    @DisplayName("Расчёт статуса Epic. Все подзадачи со статусом NEW")
    @Test
    void epicNew() {
        Epic epic1 = memoryTaskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), NEW);
    }

    @DisplayName("Расчёт статуса Epic. Все подзадачи со статусом DONE")
    @Test
    void epicDone() {
        Epic epic1 = memoryTaskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", DONE, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", DONE, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), DONE);
    }

    @DisplayName("Расчёт статуса Epic. Подзадачи со статусами NEW и DONE")
    @Test
    void epicNewAndDone() {
        Epic epic1 = memoryTaskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", DONE, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), IN_PROGRESS);

    }

    @DisplayName("Расчёт статуса Epic. Подзадачи со статусами InProgress")
    @Test
    void epicInProgress() {
        Epic epic1 = memoryTaskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", IN_PROGRESS, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        memoryTaskManager.createSubTask(new SubTask("Новая подзадача", IN_PROGRESS, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), IN_PROGRESS);
    }


    @DisplayName("Проверка наличия эпика для подзадач")
    @Test
    void checkIdOfEpicForSubTask() {
        assertEquals(memoryTaskManager.getSubTask(3).getEpic(), 2);
    }

    @DisplayName("Проверка расчета статуса для эпика")
    @Test
    void checkStatusForEpic() {
        memoryTaskManager.createSubTask(new SubTask("Купить билет на самолет обратно", DONE, "По низкой цене", epic.getId(),
                LocalDateTime.of(2025, 6, 26, 16, 0), Duration.ofMinutes(120)));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

}
