package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private Task task;
    private List<Task> listOfTasks = new ArrayList<>();
    private HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

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
}
