import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    @Test
    public void shouldReturnInitializedTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "TaskManager should be initialized and not null.");
        assertTrue(taskManager instanceof InMemoryTaskManager, "TaskManager should be an instance of InMemoryTaskManager.");
    }
}
