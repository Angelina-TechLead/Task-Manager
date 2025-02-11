import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    // Метод загрузки из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Пропустить заголовок
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки задачи из файла", e);
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
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
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtasks(int id) {
        super.deleteSubtasks(id);
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    // Метод автосохранения
    private void save() {
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(toString(task));
                writer.write("\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic));
                writer.write("\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения задач в файл", e);
        }
    }

    // Метод toString(Task task)
    private String toString(Task task) {
        String type = "TASK";
        if (task instanceof Epic) {
            type = "EPIC";
        } else if (task instanceof Subtask) {
            type = "SUBTASK";
        }
        return String.format("%d,%s,%s,%s,%s,%d",
                task.getId(),
                type,
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task instanceof Subtask ? ((Subtask) task).getEpicId() : 0
        );
    }

    // Метод fromString(String value)
    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        TaskStatus status = TaskStatus.valueOf(parts[3]);
        String description = parts[4];
        int epicId = parts.length > 5 ? Integer.parseInt(parts[5]) : 0;

        switch (type) {
            case "TASK":
                return new Task(name, description, id, status);
            case "EPIC":
                return new Epic(name, description, id, status);
            case "SUBTASK":
                return new Subtask(name, description, id, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный типа задачи: " + type);
        }
    }
}

