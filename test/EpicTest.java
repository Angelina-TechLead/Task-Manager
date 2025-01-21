import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    @Test
    public void shouldBeEqualIfIdsAreSame() {
        Epic epic1 = new Epic("Epic 1", "Description 1", 1, TaskStatus.NEW);
        Epic epic2 = new Epic("Epic 2", "Description 2", 1, TaskStatus.DONE);
        assertEquals(epic1, epic2, "Epics with the same ID should be equal.");
    }
}
