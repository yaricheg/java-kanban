package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private Task task;

    private HistoryManager historyManager;

    @BeforeEach
    void init() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void testRemoveFirst() {
        Task task1 = new Task(1, "Сходить в кино", "NEW", "С друзьями в пн");
        historyManager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", "NEW", "С друзьями в вт");
        historyManager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", "NEW", "С друзьями в ср");
        historyManager.add(task3);
        historyManager.removeFromHistory(1);
        assertEquals(historyManager.getHistory(), List.of(task2, task3));
    }

    @Test
    void testRemoveSecond() {
        Task task1 = new Task(1, "Сходить в кино", "NEW", "С друзьями в пн");
        historyManager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", "NEW", "С друзьями в вт");
        historyManager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", "NEW", "С друзьями в ср");
        historyManager.add(task3);
        historyManager.removeFromHistory(2);
        assertEquals(historyManager.getHistory(), List.of(task1, task3));
    }

    @Test
    void testRemoveLast() {
        Task task1 = new Task(1, "Сходить в кино", "NEW", "С друзьями в пн");
        historyManager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", "NEW", "С друзьями в вт");
        historyManager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", "NEW", "С друзьями в ср");
        historyManager.add(task3);
        historyManager.removeFromHistory(3);
        assertEquals(historyManager.getHistory(), List.of(task1, task2));
    }

    // Добавлено в 8 проект
    @DisplayName("Дублирование")
    @Test
    void testDuplication() {
        Task task1 = new Task(1, "Сходить в кино", "NEW", "С друзьями в пн");
        historyManager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", "NEW", "С друзьями в вт");
        historyManager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", "NEW", "С друзьями в ср");
        historyManager.add(task3);
        historyManager.add(task1); // дублируем добавление 1 задачи
        assertEquals(historyManager.getHistory(), List.of(task2, task3, task1));

    }

    @DisplayName("Пустая история задач")
    @Test
    void testEmptyHistory() {
        assertEquals(historyManager.getHistory().isEmpty(), true);
    }
}
