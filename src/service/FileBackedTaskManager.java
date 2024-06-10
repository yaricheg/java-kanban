package service;

import model.*;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void setFile(Path path) {
        file = path.toFile();
    }


    private Task fromString(String value) {
        String[] valueSplit = value.split(",");
        int id = Integer.parseInt(valueSplit[0]);
        Status status = Status.valueOf(valueSplit[3]);
        if (valueSplit[1] == "TASK") {
            return new Task(id, valueSplit[2], status, valueSplit[4]);
        }
        if (valueSplit[1] == "EPIC") {
            return new Epic(id, valueSplit[2], status, valueSplit[4]);
        }
        if (valueSplit[1] == "SUBTASK") {
            int getEpic = Integer.parseInt(valueSplit[5]);
            return new SubTask(id, valueSplit[2], status, valueSplit[4], getEpic);
        }
        return null;
    }

    // Сохранение в файл
    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // TODO Заголовок id, type, name, status, description, epic
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, SubTask> entry : subtasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            // TODO подзадачи, эпики , история
        } catch (IOException e) {
            throw new RuntimeException("Ошибка в файле: " + file.getAbsolutePath(), e);
        }

    }

    private void loadFromFile() {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {

            while (reader.ready()) {
                String line = reader.readLine();
                // Задачи
                final Task task = fromString(line);
                //TODO добавить задачу в менеджер
                final int id = task.getId();
                if (task.getType() == TaskType.TASK) {
                    tasks.put(id, task);
                } else if (task.getType() == TaskType.EPIC) {
                    epics.put(id, (Epic) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    subtasks.put(id, (SubTask) task);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String toString(Task task) {
        return "%d,%s,%s,%s,%s,%s,%d".formatted(task.getId(), task.getType(), task.getName(),
                task.getStatus(), task.getDescription(), task.getEpic());
    }


    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateTask(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subtask) { // принимать только subTask
        super.updateTask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) { // должны удалиться и подзадачи
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();//обновить статус
    }

}
