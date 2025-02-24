import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void shouldAddAndRetrieveDifferentTasks() {
        Task task = new Task("Task", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Epic epic = new Epic("Epic", "Description", 2, TaskStatus.NEW);
        Subtask subtask = new Subtask("Subtask", "Description", 3, TaskStatus.NEW, 2, Duration.ofMinutes(60), LocalDateTime.now());

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        assertEquals(task, taskManager.getTask(1));
        assertEquals(epic, taskManager.getEpic(2));
        assertEquals(subtask, taskManager.getSubtasks(3));
    }

    @Test
    public void shouldMaintainTaskHistory() {
        Task task = new Task("Task", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.addTask(task);

        taskManager.getTask(1);
        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    public void shouldNotModifyTaskOnAddition() {
        Task task = new Task("Task", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task originalTask = new Task("Task", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.addTask(task);
        assertEquals(originalTask, task, "Неизменяется задача");
    }

    @Test
    public void shouldNotConflictGeneratedAndAssignedIds() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        assertEquals(task1, taskManager.getTask(1));
        assertEquals(task2, taskManager.getTask(2));
    }

    @Test
    void noOverlapTasks() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        taskManager.addTask(task1);
        assertDoesNotThrow(() -> taskManager.addTask(task2));
    }

    @Test
    void overlapTasks() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(30));
        taskManager.addTask(task1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task2));
    }

    @Test
    public void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusHours(2));
        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        Task task3 = new Task("Task 3", "Description 3", 3, TaskStatus.NEW, Duration.ofMinutes(45), LocalDateTime.now().plusHours(1));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        assertEquals(3, prioritizedTasks.size());
        assertEquals(task2, prioritizedTasks.get(0));
        assertEquals(task3, prioritizedTasks.get(1));
        assertEquals(task1, prioritizedTasks.get(2));
    }

    @Test
    public void testGetPrioritizedTasksWithNoTasks() {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.isEmpty());
    }
}
