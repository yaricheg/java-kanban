package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {


    private ArrayList<Integer> subTasks = new ArrayList<>();
    private ArrayList<SubTask> subTasksO = new ArrayList<>();// хранить id Epic
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime = getEndTime();

    public Epic(String name, Status status, String description) {
        super(name, Status.NEW, description);
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, Status.NEW, description);

    }

    public Epic(int id, String name, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(id, name, status, description, startTime, duration);
    }


    public ArrayList<Integer> getSubTasks() {
        return subTasks;
    }

    public ArrayList<SubTask> getSubTasks0() {
        return subTasksO;
    }

    public void addSubTask(int id) {
        subTasks.add(id);
    }

    public void addSubTaskO(SubTask subTask) {
        subTasksO.add(subTask);
    }


    public void removeSubTask(Integer subTask) {
        subTasks.remove(subTask);
    }


    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }


    /* public void addTask1 (SubTask subTask){ // сложный вариант
         if(subTasks.isEmpty()){
             super.startTime = subTask.startTime;
             endTime = subTask.getEndTime();
         } else {
             if(super.startTime.isAfter(subtask.startTime)){
                 super.startTime = subTask.startTime;
             }
             LocalDateTime subEndTime = subtask.getEndTime();
             if(subEndTime.isAfter(endTime)){
                 endTime = subEndTime;
             }
         }
         subTasks.add(subTask);
         super.duration = super.duration.plus(subTask.getDuration);
     }*/
    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime startTime = LocalDateTime.of(2100, 12, 31, 12, 30);
        for (SubTask subTask : subTasksO) {
            if (subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }
        }
        return startTime;
    }

    @Override
    public Duration getDuration() {
        Duration duration = Duration.between(getStartTime(), getEndTime());
        return duration;
    }


    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = LocalDateTime.of(2000, 12, 31, 12, 30);
        for (SubTask subTask : subTasksO) {
            if (subTask.getEndTime().isAfter(endTime)) {
                endTime = subTask.getEndTime();
            }
        }
        return endTime;
    }


}
