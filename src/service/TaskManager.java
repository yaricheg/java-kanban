package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subtasks;

    private int seq = 0; // счетчик глобальный

    public TaskManager(){
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }
    private int generateId(){
        return ++seq;
    }
    //a
    public void getAllTasks(){
       System.out.println(tasks);
    }

    //добавил
    public void getAllEpics(){
        System.out.println(epics);
    }
    // добавил
    public void getAllSubTasks(){
        System.out.println(subtasks);
    }

    //b
    public void deleteAllTasks(){
        tasks.clear();
        }


    public void deleteAllEpics(){
        deleteAllSubTasks();
        epics.clear();
        }


    public void deleteAllSubTasks(){
        for(Epic epic: epics.values()){
           epic.getSubTasks().clear();
        }
        subtasks.clear();
    }


    // c
    public Task getTask(int id){
        return tasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    public SubTask getSubTask(int id){
        return subtasks.get(id);
    }

    // d
    public Task createTask (Task task){
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic (Epic epic){
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    // сначала необходимо создать эпик
    public SubTask createSubTask (SubTask subtask, Epic epic){
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubTask(subtask);
        subtask.setEpic(epic);
        calculateStatus(epic);
        return subtask;
    }

    //e
    public void updateTask (Task task){
        tasks.get(task.getId());
        tasks.put(task.getId(), task);

    }

    public void updateEpic(Epic epic){
        Epic saved  = getEpic(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        epics.put(saved.getId(), saved);
     }

    public void updateSubTask (SubTask subtask, Epic epic ){
        SubTask saved  = getSubTask(subtask.getId());
        saved.setName(subtask.getName());
        saved.setStatus(subtask.getStatus());
        deleteSubTask(subtask.getId()); // удаляем старую подзадачу из эпика
        subtasks.put(saved.getId(), saved);
        epic.addSubTask(saved); // добавляем обновленную подзадачу
        calculateStatus(epic);

    }

    //f
    public void deleteTask(int id){
        tasks.remove(id);
    }

    public void deleteEpic(int id){ // должны удалиться и подзадачи
        Epic epic = epics.get(id);
        for (SubTask subtask: epic.getSubTasks()){
            subtasks.remove(subtask.getId());
        }
        for (SubTask subtask: epic.getSubTasks()) {
            epic.removeSubTask(subtask);
        }
        epics.remove(id);
    }

    public void deleteSubTask(int id){
        SubTask subtask = getSubTask(id);
        Epic epic =  subtask.getEpic();
        epic.removeSubTask(subtask);
        subtasks.remove(id);
    }

    private void calculateStatus(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        if (epic.getSubTasks() == null){
            epic.setStatus(Status.NEW);
            return;
        }
        for (SubTask subtask: epic.getSubTasks()) {
            Status status = subtask.getStatus();
            if (status.equals(Status.NEW)) {
                statusNew += 1;
            }
            if (status.equals(Status.DONE)) {
                statusDone += 1;
            }
        }
        if (statusNew == epic.getSubTasks().size()){
            epic.setStatus(Status.NEW);
            return;
        } else if(statusDone == epic.getSubTasks().size()) {
            epic.setStatus(Status.DONE);
            return;
        } else {
            epic.setStatus(Status.IN_PROGRESS);
            return;
        }

    }
    }



