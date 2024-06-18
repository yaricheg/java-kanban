package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskTest {
    @Test
    @DisplayName("Проверка, что SubTask равны друг другу, если равен их id")
    void shouldEqualsWithCopy() {
        SubTask subTask = new SubTask("name", Status.NEW, "desc", 1,
                LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30));
        SubTask subTaskExpected = new SubTask("name", Status.NEW, "desc", 1, LocalDateTime.of(2024, 6, 12, 12, 12), Duration.ofMinutes(30));
        assertEqualsTask(subTaskExpected, subTask, "Задачи должны совпадать");
    }

    private static void assertEqualsTask(Task expected, Task actual, String message) {
        assertEquals(expected.getId(), actual.getId(), message + ", id");
        assertEquals(expected.getName(), actual.getName(), message + ", name");

    }
}
