package converter;

import model.Epic;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Converter {
    public static Task fromString(String value) {
        String[] valueSplit = value.split(",");
        int id = Integer.parseInt(valueSplit[0]);
        String status = valueSplit[3];
        LocalDateTime startDateTime = LocalDateTime.parse(valueSplit[6]);
        Duration duration = Duration.ofMinutes(Integer.parseInt(valueSplit[7]));
        if (valueSplit[1].equals("TASK")) {
            return new Task(id, valueSplit[2], status, valueSplit[4], startDateTime, duration);
        }
        if (valueSplit[1].equals("EPIC")) {
            return new Epic(id, valueSplit[2], status, valueSplit[4], startDateTime, duration);
        }
        if (valueSplit[1].equals("SUBTASK")) {
            int getEpic = Integer.parseInt(valueSplit[5]);
            return new SubTask(id, valueSplit[2], status, valueSplit[4], getEpic, startDateTime, duration);
        }
        return null;
    }

    public static String toStr(Task task) {
        return "%d,%s,%s,%s,%s,%d,%s,%d".formatted(task.getId(), task.getType(), task.getName(),
                task.getStatus(), task.getDescription(), task.getEpic(), task.getStartTime(),
                task.getDuration().toMinutes());
    }
}
