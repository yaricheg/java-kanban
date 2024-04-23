package service;

import model.Epic;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyOfTasks = new ArrayList<>();
    private final int SIZE_HISTORY_OF_TASKS = 10;

    @Override
    public void addInHistory(Task task){
        if(task == null){
            System.out.println("Введен несуществующий айди");
            return;
        }
        if(historyOfTasks.size() >= SIZE_HISTORY_OF_TASKS){
            historyOfTasks.remove(0);

        }
        historyOfTasks.add(task);
    }

    @Override //переделать
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        for (Task task: historyOfTasks) {
            historyList.add(task);
        }
        return historyList;
    }
}
