package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TaskTest {

    @Test
    @DisplayName("Проверка, что Task равны друг другу, если равен их id")
    void shouldEqualsWithCopy(){
        Task task = new Task("name",Status.NEW, "desc");
        Task taskExpected = new Task("name", Status.NEW, "desc");
        assertEqualsTask(taskExpected, task, "Задачи должны совпадать");
    }

    private static void assertEqualsTask(Task expected, Task actual, String message){
        assertEquals(expected.getId(), actual.getId(), message + ", id");
        assertEquals(expected.getName(), actual.getName(), message + ", name");

    }
}
