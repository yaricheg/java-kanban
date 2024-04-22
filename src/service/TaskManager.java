package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //a
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    ArrayList<SubTask> getSubTasksOfEpic(Epic epic);

    //b
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    // c
    void getTask(int id);

    void  getEpic(int id);

    void getSubTask(int id);

    // d
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subtask);

    //e
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subtask);

    //f
    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);

    List<Task> getHistory();

    /*void add(Task task);*/
}
