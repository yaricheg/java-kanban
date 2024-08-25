package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    List<SubTask> getEpicSubtasks(Integer id);

    List<Task> getPrioritizedTasks();

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    SubTask getSubTaskById(Integer id);

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subtask);

    void deleteTask(Integer id);

    void deleteEpic(Integer id);

    void deleteSubTask(Integer id);

    List<Task> getHistory();

}
