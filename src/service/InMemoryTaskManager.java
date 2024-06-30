package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, SubTask> subtasks;

    protected TaskComparator taskComparator = new TaskComparator();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    private final HistoryManager historyManager;

    protected int seq = 1; // счетчик глобальный

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();

    }


    protected int generateId() {
        return seq++;
    }


    private Boolean checkTaskTime(Task task) {
        if (prioritizedTasks != null) {
            for (Task t : prioritizedTasks) {
                if (t.getId() == task.getId()) {
                    continue;
                }
                if (task.getEndTime().isBefore(t.getStartTime()) ||
                        task.getStartTime().isAfter(t.getEndTime())) {
                    continue;
                }
                throw new ValidationException("Пересечение с задачей " + t.getId());
            }
            return true;
        }
        return true;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void addPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
    }

    //a
    @Override
    public List<Task> getAllTasks() {
        return tasks.values().stream().toList();
    }


    @Override
    public List<Epic> getAllEpics() {
        return epics.values().stream().toList();

    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return subtasks.values().stream().toList();
    }

    @Override
    public List<SubTask> getSubTasksOfEpic(Epic epic) {
        List<SubTask> subtasksList = epic.getSubTasks()
                .stream()
                .map(id -> subtasks.get(id))
                .collect(Collectors.toList());
        return subtasksList;


    }

    //b
    @Override
    public void deleteAllTasks() {
        tasks.keySet().stream()
                .forEach(id -> {
                    historyManager.removeFromHistory(id);
                    prioritizedTasks.remove(tasks.get(id));
                });
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.keySet().stream()
                .forEach(id -> {
                    historyManager.removeFromHistory(id);
                    prioritizedTasks.remove(subtasks.get(id));
                });
        epics.keySet().stream()
                .forEach(id -> historyManager.removeFromHistory(id));
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subtasks.keySet().stream()
                .forEach(id -> {
                    historyManager.removeFromHistory(id);
                    prioritizedTasks.remove(subtasks.get(id));
                });
        epics.values().stream()
                .forEach(epic -> {
                    epic.getSubTasks().clear();
                    epic.setStartTime(null);
                    epic.setEndTime(null);

                });
        subtasks.clear();

    }

    // c
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Задача с ид " + id + " не найдена");
        }
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
        checkTaskTime(task);
        addPrioritizedTasks(task); // добавил
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
        checkTaskTime(subtask);
        addPrioritizedTasks(subtask);// добавил
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpic());
        epic.addSubTask(subtask.getId());
        getTimeEpic(epic);
        calculateStatus(epic);
        return subtask;
    }

    //e
    @Override
    public void updateTask(Task task) throws NotFoundException {
        if (tasks.get(task.getId()) == null) {
            throw new NotFoundException("Задача не найдена ");
        }
        checkTaskTime(task);
        for (Task taskOld : tasks.values()) {
            if (taskOld.getId() == task.getId()) {
                prioritizedTasks.remove(taskOld); //O(logn)
            }
        }
        prioritizedTasks.add(task); //O(logn)
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
        checkTaskTime(subtask);
        prioritizedTasks.remove(saved);
        prioritizedTasks.add(subtask);
        subtasks.put(subtask.getId(), subtask);
        //O(logn)
        Epic epic = epics.get(subtask.getEpic());
        if (epic == null) {
            throw new NotFoundException("Подзадача не найдена ");
        }
        epic.addSubTask(subtask.getId()); // добавляем обновленную подзадачу
        getTimeEpic(epic);
        calculateStatus(epic); // обновить статус эпика
    }

    //f
    @Override
    public void deleteTask(int id) {
        historyManager.removeFromHistory(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) { // должны удалиться и подзадачи
        epics.get(id).getSubTasks().stream()
                .forEach(idSubtask -> {
                    prioritizedTasks.remove(subtasks.get(idSubtask));
                    subtasks.remove(idSubtask);
                    historyManager.removeFromHistory(idSubtask);
                });
        historyManager.removeFromHistory(id);
        epics.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpic());
        epic.removeSubTask(id);
        historyManager.removeFromHistory(id);
        prioritizedTasks.remove(subtasks.get(id));
        getTimeEpic(epic);
        subtasks.remove(id);
        calculateStatus(epic);//обновить статус
    }

    private void calculateStatus(Epic epic) {
        if (epic.getSubTasks() == null) {
            epic.setStatus(Status.NEW);
            return;
        }
        ArrayList<Status> statusNew = new ArrayList<>();
        ArrayList<Status> statusDone = new ArrayList<>();
        epic.getSubTasks().stream()
                .forEach(id -> {
                    SubTask subTask = subtasks.get(id);
                    Status status = subTask.getStatus();
                    if (status.equals(Status.NEW)) {
                        statusNew.add(status);
                    }
                    if (status.equals(Status.DONE)) {
                        statusDone.add(status);
                    }
                });
        if (statusNew.size() == epic.getSubTasks().size()) {
            epic.setStatus(Status.NEW);
        } else if (statusDone.size() == epic.getSubTasks().size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    private LocalDateTime getTimeEpic(Epic epic) {
        LocalDateTime startTime = LocalDateTime.of(2100, 12, 31, 12, 30);
        LocalDateTime endTime = LocalDateTime.of(2000, 12, 31, 12, 30);
        for (Integer id : epic.getSubTasks()) {
            if (subtasks.get(id).getStartTime().isBefore(startTime)) {
                startTime = subtasks.get(id).getStartTime();
            }
            if (subtasks.get(id).getStartTime().plus(subtasks.get(id).getDuration()).isAfter(endTime)) {
                endTime = subtasks.get(id).getStartTime().plus(subtasks.get(id).getDuration());
            }

        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        Duration duration = Duration.between(startTime, endTime);
        epic.setDuration(duration);
        return endTime;
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}








