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

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    // По заданию
    @Override
    @BeforeEach
    void init() {
        taskManager = (InMemoryTaskManager) Managers.getDefaults();
        task = taskManager.createTask(new Task("Новая задача", NEW, "Задача 1",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        epic = taskManager.createEpic(new Epic(6, "Новый Эпик", NEW, "Задача 1",
                LocalDateTime.of(2027, 7, 12, 12, 12), Duration.ofMinutes(30)));
        subTask = taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic.getId(),
                LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30)));
    }


    @DisplayName("Проверяется, что InMemoryTaskManager добавляет задачи разного типа и может найти их по id")
    @Test
    void shouldCreateTaskAndSearchId() {
        assertEquals(task, taskManager.getTask(task.getId()));
        assertEquals(epic, taskManager.getEpic(epic.getId()));
        assertEquals(subTask, taskManager.getSubTask(subTask.getId()));

    }

    @DisplayName("Проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    @Test
    void shouldBeTrueTasks() {
        assertEquals(task, taskManager.getTask(task.getId()));
        assertEquals(epic, taskManager.getEpic(epic.getId()));
        assertEquals(subTask, taskManager.getSubTask(subTask.getId()));

    }

    @DisplayName("Проверяется, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    @Test
    void shouldBeFalseIdTasks() {
        Task task1 = taskManager.createTask(new Task(1, "Простая задача", NEW, "2+2",
                LocalDateTime.of(2028, 7, 12, 12, 12), Duration.ofMinutes(30)));
        boolean check = task1.getId() == task.getId();
        assertFalse(check);
    }

    @DisplayName("HistoryManager должен сохранять последнюю версию задачи и ее данных")
    @Test
    void shouldCheckTasksInHistoryManager() {
        taskManager.updateTask(new Task(task.getId(), "Старая задача", DONE, "Задача 1 выполнена",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        taskManager.getTask(task.getId());
        assertEquals(taskManager.getAllTasks(), taskManager.getHistory(), "Значения должны совпадать");

    }


    @DisplayName("Проверка удаления задачи из менеджера")
    @Test
    void shouldBe0DeleteTaskFromManager() {
        taskManager.deleteTask(task.getId());
        assertEquals(0, taskManager.getAllTasks().size());
    }

    @DisplayName("Проверка удаления Эпика из менеджера")
    @Test
    void shouldBe0DeleteEpicFromManager() {
        taskManager.deleteEpic(epic.getId());
        assertEquals(0, taskManager.getAllEpics().size());
    }

    @DisplayName("Проверка удаления Подзадачи из менеджера")
    @Test
    void shouldBe0DeleteSubTaskFromManager() {
        taskManager.deleteSubTask(subTask.getId());
        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @DisplayName("Проверка обновления задачи в менеджере")
    @Test
    void shouldBeFalseUpdateTask() {
        taskManager.updateTask(new Task(task.getId(), "Старая задача", DONE, "Выполнена",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        boolean check = task.equals(taskManager.getTask(task.getId()));
        assertFalse(check);
    }

    public boolean equalsEpic(Epic epic1) {
        return epic1.equals(epic);
    }

    @DisplayName("Проверка обновления эпика в менеджере")
    @Test
    void shouldBeTrueUpdateEpic() {
        taskManager.updateEpic(new Epic(epic.getId(), "Старая задача", NEW, "Выполнена"));
        boolean check = equalsEpic(taskManager.getEpic(epic.getId()));
        assertTrue(check);
    }

    // Добавлено в 8 проект
    @DisplayName("Расчёт статуса Epic. Все подзадачи со статусом NEW")
    @Test
    void epicNew() {
        Epic epic1 = taskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), NEW);
    }

    @DisplayName("Расчёт статуса Epic. Все подзадачи со статусом DONE")
    @Test
    void epicDone() {
        Epic epic1 = taskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        taskManager.createSubTask(new SubTask("Новая подзадача", DONE, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        taskManager.createSubTask(new SubTask("Новая подзадача", DONE, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), DONE);
    }

    @DisplayName("Расчёт статуса Epic. Подзадачи со статусами NEW и DONE")
    @Test
    void epicNewAndDone() {
        Epic epic1 = taskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        taskManager.createSubTask(new SubTask("Новая подзадача", DONE, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), IN_PROGRESS);

    }

    @DisplayName("Расчёт статуса Epic. Подзадачи со статусами InProgress")
    @Test
    void epicInProgress() {
        Epic epic1 = taskManager.createEpic(new Epic("Новый Эпик", NEW, "Задача 1"));
        taskManager.createSubTask(new SubTask("Новая подзадача", IN_PROGRESS, "подзадача 1", epic1.getId(),
                LocalDateTime.of(2025, 6, 12, 12, 12), Duration.ofMinutes(30)));
        taskManager.createSubTask(new SubTask("Новая подзадача", IN_PROGRESS, "подзадача 2", epic1.getId(),
                LocalDateTime.of(2026, 6, 12, 12, 12), Duration.ofMinutes(30)));
        assertEquals(epic1.getStatus(), IN_PROGRESS);
    }


    @DisplayName("Проверка наличия эпика для подзадач")
    @Test
    void checkIdOfEpicForSubTask() {
        assertEquals(taskManager.getSubTask(subTask.getId()).getEpic(), epic.getId());
    }

    @DisplayName("Проверка расчета статуса для эпика")
    @Test
    void checkStatusForEpic() {
        taskManager.createSubTask(new SubTask("Купить билет на самолет обратно", DONE, "По низкой цене", epic.getId(),
                LocalDateTime.of(2025, 6, 26, 16, 0), Duration.ofMinutes(120)));
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

}
