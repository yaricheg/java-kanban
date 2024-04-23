package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private Task task;
    private Managers managers = new Managers();
    private InMemoryTaskManager inMemoryTaskManager = managers.getDefaults();
    private InMemoryHistoryManager inMemoryHistoryManager = managers.getDefaultHistory();

    @BeforeEach
    void BeforeEach() {

        for (int i = 1; i <= 12; i++) {
            Task task = inMemoryTaskManager.createTask(new Task(i, "Новая задача",
                    Status.NEW, "Задача " + i));
            inMemoryHistoryManager.addInHistory(task);

        }
    }

    @Test
    void checkAdd() {
        assertEquals(10, inMemoryHistoryManager.getHistory().size());
    }

    @Test
    void checkGetHistory(){
        Task taskCheck = inMemoryTaskManager.getTask(3);
        assertEquals(taskCheck, inMemoryHistoryManager.getHistory().get(0));
    }
}
