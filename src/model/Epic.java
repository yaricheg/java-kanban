package model;


import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    ArrayList<SubTask> SubTasks = new ArrayList<>();
    public Epic(String name,  String description){
        super(name, Status.NEW, description);
    }

    public Epic (int id, String name, Status status, String description) {
        super(id, name, status, description);
    }


    public ArrayList<SubTask> getSubTasks() {
        return SubTasks;
    }

    public void addSubTask(SubTask subTask) {

        SubTasks.add(subTask);

    }

    public void removeSubTask(SubTask subTask){

        SubTasks.remove(subTask);
    }







}
