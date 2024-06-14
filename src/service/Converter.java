package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

public class Converter {
    protected static Task fromString(String value) {
        String[] valueSplit = value.split(",");
        int id = Integer.parseInt(valueSplit[0]);
        Status status = Status.valueOf(valueSplit[3]);
        if (valueSplit[1].equals("TASK")) {
            return new Task(id, valueSplit[2], status, valueSplit[4]);
        }
        if (valueSplit[1].equals("EPIC")) {
            return new Epic(id, valueSplit[2], status, valueSplit[4]);
        }
        if (valueSplit[1].equals("SUBTASK")) {
            int getEpic = Integer.parseInt(valueSplit[5]);
            return new SubTask(id, valueSplit[2], status, valueSplit[4], getEpic);
        }
        return null;
    }

    protected static String toStr(Task task) {
        return "%d,%s,%s,%s,%s,%d".formatted(task.getId(), task.getType(), task.getName(),
                task.getStatus(), task.getDescription(), task.getEpic());
    }

}
