package model;
public class SubTask extends Task {
    private int idEpic;
    public SubTask(String name, Status status, String description,int idEpic) { // для создания подзадачи
        super(name, status, description);
        this.idEpic = idEpic;
    }

    public SubTask(int id, String name, Status status, String description) {
        super(id, name, status, description);
    }

    public int getEpic() {
        return idEpic;
    }

    public void setEpic(int idEpic) {
        this.idEpic = idEpic;
    } // стоит задавать при создании подзадачи в конструкторе
}
