package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static model.TaskType.*;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }


    // Сохранение в файл
    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            // TODO Заголовок id, type, name, status, description, epic
            writer.append("id,type,name,status,description,epic\n");
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(Converter.toStr(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(Converter.toStr(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Integer, SubTask> entry : subtasks.entrySet()) {
                writer.append(Converter.toStr(entry.getValue()));
                writer.newLine();
            }
            // TODO подзадачи, эпики , история
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath());
        }

    }


    public static FileBackedTaskManager loadFromFile(File file) {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            String lineHead = reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                // Задачи
                final Task task = Converter.fromString(line);
                //TODO добавить задачу в менеджер
                final int id = task.getId();
                if (task.getType().equals(TASK)) {
                    fileBackedTaskManager.tasks.put(id, task);
                } else if (task.getType().equals(EPIC)) {
                    fileBackedTaskManager.epics.put(id, (Epic) task);
                } else if (task.getType().equals(SUBTASK)) {
                    fileBackedTaskManager.subtasks.put(id, (SubTask) task);
                    Epic epic = fileBackedTaskManager.epics.get(task.getEpic());
                    epic.addSubTask(task.getId());
                }
            }
            return fileBackedTaskManager;

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле: " + file.getAbsolutePath());
        }
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
        super.updateEpic(epic);
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
