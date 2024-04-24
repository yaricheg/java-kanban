package service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @DisplayName("Проверяется,что утилитарный класс всегда возвращает готовые к работе экземпляры менеджеров")
    @Test
    void InMemoryTaskManagerTest(){
        assertNotNull(Managers.getDefaults());
    }

    @Test
    void InMemoryHistoryManagerTest(){
        assertNotNull(Managers.getDefaultHistory());
    }
}
