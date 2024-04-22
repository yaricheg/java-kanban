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

    @BeforeEach
    void BeforeEach() {

        for (int i = 1; i <= 12; i++) {
            Task task = inMemoryTaskManager.createTask(new Task(i, "Новая задача",
                    Status.NEW, "Задача " + i));
            inMemoryTaskManager.getTask(i);

        }
    }

    @Test
    void checkAdd() {
        assertEquals(10, inMemoryTaskManager.getHistory().size());
    }

    @Test
    void checkGetHistory(){
        Task taskCheck = inMemoryTaskManager.tasks.get(3);
        assertEquals(taskCheck, inMemoryTaskManager.getHistory().get(0));
    }
}
