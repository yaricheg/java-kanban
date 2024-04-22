package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    HashMap<Integer, Task> tasks; // удалить private для сравнения списков
    HashMap<Integer, Epic> epics;
    HashMap<Integer, SubTask> subtasks;
    //private List<Task> historyOfTasks; // поменять тип объекта в листе
    private final HistoryManager historyManager;
    private int seq = 0; // счетчик глобальный

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    private int generateId() {
        return ++seq;
    }

    //a
    @Override
    public List<Task> getAllTasks() {
        List<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }


    @Override
    public List<Epic> getAllEpics() {
        List<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        List<SubTask> subtasksList = new ArrayList<>();
        for (SubTask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(Epic epic) {
        ArrayList<SubTask> subtasksList = new ArrayList<>();
        for (Integer subtask : epic.getSubTasks()) {
            subtasksList.add(subtasks.get(subtask));
        }
        return subtasksList;
    }

    //b
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }


    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }


    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
        }
        subtasks.clear();
    }


    // c
    @Override
    public void getTask(int id) {
        historyManager.add(tasks.get(id));
    }

    @Override
    public void getEpic(int id) {
        historyManager.add(epics.get(id));
    }

    @Override
    public void getSubTask(int id) {
        historyManager.add(subtasks.get(id));
    }

    // d
    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }


    @Override
    public SubTask createSubTask(SubTask subtask) {
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic());
        epic.addSubTask(subtask.getId());
        calculateStatus(epic);
        return subtask;
    }

    //e
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);

    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        epics.put(saved.getId(), saved);
    }

    @Override
    public void updateSubTask(SubTask subtask) { // принимать только subTask
        SubTask saved = subtasks.get(subtask.getId());
        saved.setName(subtask.getName());
        saved.setStatus(subtask.getStatus());
        Epic epic = epics.get(subtask.getEpic());
        epic.addSubTask(subtask.getId());// добавляем обновленную подзадачу
        calculateStatus(epic); // обновить статус эпика
    }

    //f
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) { // должны удалиться и подзадачи
        Epic epic = epics.get(id);
        for (Integer subtaskId : epic.getSubTasks()) {
            subtasks.remove(subtaskId);
        }

        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpic());
        epic.removeSubTask(id);
        subtasks.remove(id);
        calculateStatus(epic);//обновить статус
    }

    private void calculateStatus(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        if (epic.getSubTasks() == null) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (Integer subtaskId : epic.getSubTasks()) {
            SubTask subTask = subtasks.get(subtaskId);
            Status status = subTask.getStatus();
            if (status.equals(Status.NEW)) {
                statusNew += 1;
            }
            if (status.equals(Status.DONE)) {
                statusDone += 1;
            }
        }
        if (statusNew == epic.getSubTasks().size()) {
            epic.setStatus(Status.NEW);

        } else if (statusDone == epic.getSubTasks().size()) {
            epic.setStatus(Status.DONE);

        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }


  /*  public void add(Task task){
        if(historyOfTasks.size() < 10){
            historyOfTasks.add(task);
        }
        else{
            historyOfTasks.remove(0);
            historyOfTasks.add(task);
        }
    }*/
}



