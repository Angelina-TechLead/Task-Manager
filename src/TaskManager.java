import java.util.List;

public interface TaskManager {
    static Object getTasks() {
        return null;
    }

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    Task getTask(int id);

    Task getEpic(int id);

    Task getSubtasks(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtasks(int id);

    void deleteSubtask(int subtaskId);

    List<Subtask> getSubtasksOfEpic(int epicId);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}


