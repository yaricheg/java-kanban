package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> historyOfTasks = new ArrayList<>();
    @Override
    public void add(Task task){
        if (historyOfTasks == null){
            historyOfTasks.add(task);
        }
        if(historyOfTasks.size() < 10){
            historyOfTasks.add(task);
        }
        else{
            historyOfTasks.remove(0);
            historyOfTasks.add(task);
        }
    }

    @Override //переделать
    public List<Task> getHistory() {
        return historyOfTasks;
    }


}
