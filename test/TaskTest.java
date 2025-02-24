import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void shouldBeEqualIfIdsAreSame() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", 1, TaskStatus.DONE, Duration.ofMinutes(60), LocalDateTime.now());
        assertEquals(task1, task2, "Задачи одинаковые, но с одинаковым ID");
    }

    @Test
    public void shouldNotBeEqualIfIdsAreDifferent() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.DONE, Duration.ofMinutes(60), LocalDateTime.now());
        assertNotEquals(task1, task2, "Задачи не одинаковые");
    }
}
