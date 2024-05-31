package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskType;

import java.io.*;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    // HashMap<TaskType, TaskConverter>;
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }
   /* public FileBackedTaskManager(){this(Managers.getDefaultHistory());}
    public FileBackedTaskManager(HistoryManager historyManager){this(historyManager,
            new File());}

    public FileBackedTaskManager(File file){this(Managers.getDefaultHistory(),file);}*/

    private Task fromString(String value) {
        return task;
    }

    // Сохранение в файл
    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file)) {
            // TODO Заголовок id, type, name, status, description, epic
            for(
            Map.Entry<Integer, Task> entry:tasks.entrySet())

            {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            // TODO подзадачи, эпики , история
        } catch(IOException e) {
            throw new RuntimeException("Ошибка в файле: " + file.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void loadFromFile() {
        int maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileWriter(file))) {
            reader.readLine();
            while (true) {
                String line = reader.readLine();
                // Задачи
                final Task task = fromString(line);
                //TODO добавить задачу в менеджер
                final int id = task.getId();
                if (task.getType() == TaskType.TASK) {
                    tasks.put(id, task);
                }
               /*else if(task.getType() == TaskType.EPIC){
                    epics.put(id,task );
                }
                else if (task.getType() == TaskType.SUBTASK){
                    subtasks.put(id, task)
                }*/// TODO
            }
        }
    }
}


private String toString(Task task) {
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

}
