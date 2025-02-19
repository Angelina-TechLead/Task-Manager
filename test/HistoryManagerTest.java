import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    public void testRemoveTaskFromHistory() {
        Task task1 = new Task("Task 1", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(60));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    public void testRemoveTaskFromHistoryBeginning() {
        Task task1 = new Task("Task 1", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(60));
        Task task3 = new Task("Task 3", "Description", 3, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        assertFalse(history.contains(task1));
        assertTrue(history.contains(task2));
        assertTrue(history.contains(task3));
    }

    @Test
    public void testRemoveTaskFromHistoryMiddle() {
        Task task1 = new Task("Task 1", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(60));
        Task task3 = new Task("Task 3", "Description", 3, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        List<Task> history = historyManager.getHistory();
        assertTrue(history.contains(task1));
        assertFalse(history.contains(task2));
        assertTrue(history.contains(task3));
    }

    @Test
    public void testRemoveTaskFromHistoryEnd() {
        Task task1 = new Task("Task 1", "Description", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description", 2, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(60));
        Task task3 = new Task("Task 3", "Description", 3, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now().plusMinutes(120));
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        List<Task> history = historyManager.getHistory();
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
        assertFalse(history.contains(task3));
    }
}
