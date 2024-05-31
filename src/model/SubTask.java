package model;

public class SubTask extends Task {
    private int idEpic;

    public SubTask(String name, Status status, String description, int idEpic) { // для создания подзадачи
        super(name, status, description);
        this.idEpic = idEpic;
    }

    public SubTask(int id, String name, Status status, String description, int epicId) {
        super(id, name, status, description);
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
