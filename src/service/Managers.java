package service;

import java.io.File;

public class Managers {
    public static TaskManager getDefaults() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultsFile(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }
}
