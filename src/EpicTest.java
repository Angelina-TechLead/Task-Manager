import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    @Test
    public void shouldBeEqualIfIdsAreSame() {
        Epic epic1 = new Epic("Epic 1", "Description 1", 1, TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Description 2", 1, TaskStatus.DONE);
        assertEquals(epic1, epic2, "Epics with the same ID should be equal.");
    }

    @Test
    public void shouldNotAddSubtaskWithSameId() {
        Epic epic = new Epic("Epic", "Description", 1, TaskStatus.NEW);
        Subtask subtask = new Subtask("Subtask", "Description", 1, TaskStatus.IN_PROGRESS, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtask(subtask);
        }, "Epic should not add subtask with the same ID.");
    }
}
