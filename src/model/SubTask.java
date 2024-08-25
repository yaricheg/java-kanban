package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, String status, String description, int idEpic, LocalDateTime startTime, Duration duration) { // для создания подзадачи
        super(name, status, description, startTime, duration);
        this.idEpic = idEpic;
    }

    public SubTask(int id, String name, String status, String description, int idEpic, LocalDateTime startTime, Duration duration) {
        super(id, name, status, description, startTime, duration);
        this.idEpic = idEpic;
    }

    @Override
    public Integer getEpic() {
        return idEpic;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public void setEpic(int idEpic) {
        this.idEpic = idEpic;
    } // стоит задавать при создании подзадачи в конструкторе
}
