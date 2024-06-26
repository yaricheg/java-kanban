package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTasks = new ArrayList<>();

    public Epic(String name, Status status, String description) {
        super(name, Status.NEW, description);
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, Status.NEW, description);
    }

    public Epic(String name, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(name, Status.NEW, description, startTime, duration);
    }

    public Epic(int id, String name, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(id, name, status, description, startTime, duration);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(int id) {
        subTasks.add(id);
    }

    public void removeSubTask(Integer subTask) {
        subTasks.remove(subTask);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

}
