package service;

public class Managers {
    public static TaskManager getDefaults() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultsFile() {
        return new FileBackedTaskManager(getDefaultHistory());
    }
}
