package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {


    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, SubTask> subtasks;

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
        for (int i : tasks.keySet()) {
            historyManager.removeFromHistory(i);
        }
        tasks.clear();

    }


    @Override
    public void deleteAllEpics() {
        for (int i : subtasks.keySet()) {
            historyManager.removeFromHistory(i);
        }
        for (int i : epics.keySet()) {
            historyManager.removeFromHistory(i);
        }
        subtasks.clear();
        epics.clear();
    }


    @Override
    public void deleteAllSubTasks() {
        for (int i : subtasks.keySet()) {
            historyManager.removeFromHistory(i);
        }
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
        }
        subtasks.clear();
    }


    // c
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = subtasks.get(id);
        historyManager.add(subTask);
        return subTask;
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
    public void updateTask(Task task) throws NotFoundException {
        if (tasks.get(task.getId()) == null) {
            throw new NotFoundException("Задача не найдена ");
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            throw new NotFoundException("Эпик не найден ");
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
        if (epic == null) {
            throw new NotFoundException("Эпик не найден ");
        }
        epic.addSubTask(subtask.getId());// добавляем обновленную подзадачу
        calculateStatus(epic); // обновить статус эпика
    }

    //f
    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.removeFromHistory(id);

    }

    @Override
    public void deleteEpic(int id) { // должны удалиться и подзадачи
        Epic epic = epics.get(id);
        for (Integer subtaskId : epic.getSubTasks()) {
            subtasks.remove(subtaskId);
            historyManager.removeFromHistory(subtaskId);
        }
        historyManager.removeFromHistory(id);
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpic());
        epic.removeSubTask(id);
        historyManager.removeFromHistory(id);
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void loadFromFile() {
        System.out.println("Выберите другой класс:");
    }

}




