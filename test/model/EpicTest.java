package model;
import model.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Эпик")
public class EpicTest {
    @Test
    @DisplayName("Проверка, что эпики равны друг другу, если равен их id")
    void shouldEqualsWithCopy(){
        Epic epic = new Epic("name",  "desc");
        Epic epicExpected = new Epic("name",  "desc");
        assertEqualsTask(epicExpected, epic, "эпики должны совпадать");
    }

    private static void assertEqualsTask(Task expected, Task actual, String message){
        assertEquals(expected.getId(), actual.getId(), message + ", id");
        assertEquals(expected.getName(), actual.getName(), message + ", name");

    }
}