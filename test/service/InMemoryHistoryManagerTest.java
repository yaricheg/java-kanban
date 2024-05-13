package service;

import model.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private Task task;
    private List<Task> listOfTasks = new ArrayList<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    /*
    @BeforeEach
    void BeforeEach() {

        for (int i = 1; i <= 12; i++) {
            task = new Task(i, "Новая задача", Status.NEW, "Задача " + i);
            listOfTasks.add(task);
            inMemoryHistoryManager.addInHistory(task);

        }
    }

    @Test
    void checkAdd() {
        assertEquals(10, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void checkGetHistory(){
        Task taskCheck = listOfTasks.get(11);
        assertEquals(taskCheck, inMemoryHistoryManager.getHistory().get(9));
    }
    */


    // появилось

    @Test
    void testRemoveFirst() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Сходить в кино", NEW, "С друзьями в пн");
        manager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", NEW, "С друзьями в вт");
        manager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", NEW, "С друзьями в ср");
        manager.add(task3);

        manager.removeFromHistory(1);
        assertEquals(manager.getHistory(), List.of(task2, task3));
    }

    @Test
    void testRemoveSecond() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Сходить в кино", NEW, "С друзьями в пн");
        manager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", NEW, "С друзьями в вт");
        manager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", NEW, "С друзьями в ср");
        manager.add(task3);

        manager.removeFromHistory(2);
        assertEquals(manager.getHistory(), List.of(task1, task3));
    }

    @Test
    void testRemoveLast() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Сходить в кино", NEW, "С друзьями в пн");
        manager.add(task1);
        Task task2 = new Task(2, "Сходить в кино", NEW, "С друзьями в вт");
        manager.add(task2);
        Task task3 = new Task(3, "Сходить в кино", NEW, "С друзьями в ср");
        manager.add(task3);

        manager.removeFromHistory(3);
        assertEquals(manager.getHistory(), List.of(task1, task2));

    }
}
