package service;

import exception.ValidationException;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    Task task;
    Epic epic;
    SubTask subTask;
    T taskManager;

    @BeforeEach
    void init() throws IOException {
        task = taskManager.createTask(new Task("Новая задача", NEW, "Задача 1",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        epic = taskManager.createEpic(new Epic(6, "Новый Эпик", NEW, "Задача 1",
                LocalDateTime.of(2027, 7, 12, 12, 12), Duration.ofMinutes(30)));
        subTask = taskManager.createSubTask(new SubTask("Новая подзадача", NEW, "подзадача 1", epic.getId(),
                LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30)));
    }

    @Test
    void getAllTasks() {
        assertEquals(taskManager.getTasks().size(), 1);

    }

    @Test
    void getAllEpics() {
        assertEquals(taskManager.getEpics().size(), 1);
    }

    @Test
    void getAllSubTasks() {
        assertEquals(taskManager.getSubTasks().size(), 1);
    }

    @Test
    void getSubTasksOfEpic() {
        SubTask subTask1 = taskManager.getEpicSubtasks(2).get(0);
        assertTrue(subTask.equals(subTask1));
    }

    @Test
    void getPrioritizedTasks() {
        assertEquals(taskManager.getPrioritizedTasks().get(0).equals(subTask), true);
        assertEquals(taskManager.getPrioritizedTasks().get(1).equals(task), true);

    }

    @Test
    void deleteAllTasks() {
        taskManager.deleteTasks();
        assertEquals(taskManager.getTasks().size(), 0);
    }

    @Test
    void deleteAllEpics() {
        taskManager.deleteEpics();
        assertEquals(taskManager.getEpics().size(), 0);
        assertEquals(taskManager.getSubTasks().size(), 0);
    }

    @Test
    void deleteAllSubTasks() {
        taskManager.deleteSubTasks();
        assertEquals(taskManager.getEpics().size(), 1);
        assertEquals(taskManager.getSubTasks().size(), 0);
    }

    @Test
    void getTask() {
        taskManager.getTaskById(task.getId());
        assertEquals(taskManager.getHistory().get(0), task);
    }

    @Test
    void getEpic() {
        taskManager.getEpicById(epic.getId());
        assertEquals(taskManager.getHistory().get(0), epic);
    }

    @Test
    void getSubTask() {
        taskManager.getSubTaskById(subTask.getId());
        assertEquals(taskManager.getHistory().get(0), subTask);
    }

    @Test
    void createTask() {
        assertEquals(taskManager.getTasks().size(), 1);
    }

    @Test
    void createEpic() {
        assertEquals(taskManager.getEpics().size(), 1);
    }

    @Test
    void createSubTask() {
        assertEquals(taskManager.getSubTasks().size(), 1);
    }

    @Test
    void updateTask() {
        Task taskOld = task;
        taskManager.updateTask(new Task(task.getId(), "Cуперновая задача", NEW, "Задача 1",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30)));
        assertFalse(taskManager.getTasks().get(0).equals(taskOld));

    }

    @Test
    void updateEpic() {
        Epic newEpic = new Epic(epic.getId(), "Cуперновый эпик", NEW, "Суперэпик 1",
                LocalDateTime.of(2024, 7, 12, 12, 12), Duration.ofMinutes(30));
        taskManager.updateEpic(newEpic);
        assertTrue(taskManager.getEpics().get(0).equals(newEpic));

    }

    @Test
    void updateSubTask() {
        SubTask newSubTask = new SubTask(subTask.getId(), "Суперновая подзадача", NEW,
                "Суперописание", epic.getId(),
                LocalDateTime.of(2025, 7, 12, 12, 12), Duration.ofMinutes(30));
        taskManager.updateSubTask(newSubTask);
        assertTrue(taskManager.getEpicSubtasks(2).get(0).equals(newSubTask));


    }

    @Test
    void deleteTask() {
        taskManager.deleteTask(task.getId());
        assertEquals(taskManager.getTasks().size(), 0);
    }

    @Test
    void deleteEpic() {
    }

    @Test
    void deleteSubTask() {
        taskManager.deleteSubTask(subTask.getId());
        assertEquals(taskManager.getSubTasks().size(), 0);
    }

    @Test
    void getHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubTaskById(subTask.getId());
        assertEquals(taskManager.getHistory().size(), 3);
    }


    @DisplayName("Проверка на пересечение задач")
    @Test
    void testValidationException() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            taskManager.createTask(new Task("Новая задача", NEW, "Задача 1",
                    LocalDateTime.of(2024, 7, 12, 12, 15), Duration.ofMinutes(30)));
        }, "Пересечение с задачей " + task.getId());
        assertEquals("Пересечение с задачей " + task.getId(), thrown.getMessage());
    }
}