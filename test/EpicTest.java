import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private TaskManager taskManager;
    private Epic epic;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        epic = new Epic("Epic 1", "Description", 1, TaskStatus.NEW);
        taskManager.addEpic(epic);
    }

    @Test
    public void allSubtasksNew() {
        taskManager.addSubtask(new Subtask("Subtask 1", "Description", 2, TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.addSubtask(new Subtask("Subtask 2", "Description", 3, TaskStatus.NEW, epic.getId(), Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(30)));
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void allSubtasksDone() {
        taskManager.addSubtask(new Subtask("Subtask 1", "Description", 2, TaskStatus.DONE, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.addSubtask(new Subtask("Subtask 2", "Description", 3, TaskStatus.DONE, epic.getId(), Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(30)));
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void subtaskNewAndDone() {
        taskManager.addSubtask(new Subtask("Subtask 1", "Description", 2, TaskStatus.NEW, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.addSubtask(new Subtask("Subtask 2", "Description", 3, TaskStatus.DONE, epic.getId(), Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(30)));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void allSubtasksInProgress() {
        taskManager.addSubtask(new Subtask("Subtask 1", "Description", 2, TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.addSubtask(new Subtask("Subtask 2", "Description", 3, TaskStatus.IN_PROGRESS, epic.getId(), Duration.ofMinutes(45), LocalDateTime.now().plusMinutes(30)));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}
