package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    List<SubTask> getSubTasksOfEpic(Epic epic);

    List<Task> getPrioritizedTasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubTasks();

    Task getTask(int id);

    Epic getEpic(int id);

    SubTask getSubTask(int id);

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubTask(int id);

    List<Task> getHistory();

}
