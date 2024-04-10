package model;

public class SubTask extends Task {

    private Epic epic;
    public SubTask(String name, Status status, String description) {
        super(name, status, description);
    }
    public SubTask (int id, String name, Status status, String description) {
        super(id, name, status, description);
    }
    @Override
    public Epic getEpic(){
        return epic;
    }
    public void setEpic(Epic epic) {
        this.epic = epic;
    }




}
