package service;

public class Managers {
    public static InMemoryTaskManager getDefaults(){
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
