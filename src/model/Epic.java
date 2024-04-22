package model;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<Integer> subTasks = new ArrayList<>(); // хранить id Epic

    public Epic(String name, String description) {
        super(name, Status.NEW, description);
    }

    public Epic(int id, String name, Status status, String description) {
        super(id, name, status, description);
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


}
