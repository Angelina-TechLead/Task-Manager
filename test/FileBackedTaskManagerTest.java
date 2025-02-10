import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    void tearDown() {
        tempFile.delete();
    }

    @Test
    void addAndSaveTask() {
        Task task = new Task("Task 1", "Description 1", 1, TaskStatus.NEW);
        manager.addTask(task);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));

        manager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = manager.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test
    void addAndSaveEpic() {
        Epic epic = new Epic("Epic 1", "Description 1", 1, TaskStatus.NEW);
        manager.addEpic(epic);

        List<Epic> epics = manager.getAllEpics();
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(0));

        manager = FileBackedTaskManager.loadFromFile(tempFile);
        epics = manager.getAllEpics();
        assertEquals(1, epics.size());
        assertEquals(epic, epics.get(0));
    }

    @Test
    void addAndSaveSubtask() {
        Epic epic = new Epic("Epic 1", "Description 1", 1, TaskStatus.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description 1", 2, TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals(subtask, subtasks.get(0));

        manager = FileBackedTaskManager.loadFromFile(tempFile);
        subtasks = manager.getAllSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals(subtask, subtasks.get(0));
    }

    @Test
    void testSaveAndLoadMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1", 1, TaskStatus.NEW);
        Task task2 = new Task("Task 2", "Description 2", 2, TaskStatus.DONE);
        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));

        manager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        manager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = manager.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testSaveAndLoadAfterDeletingTask() {
        Task task = new Task("Task 1", "Description 1", 1, TaskStatus.NEW);
        manager.addTask(task);
        manager.deleteTask(task.getId());

        List<Task> tasks = manager.getAllTasks();
        assertTrue(tasks.isEmpty());

        manager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = manager.getAllTasks();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void testSaveAndLoadAfterUpdatingTask() {
        Task task = new Task("Task 1", "Description 1", 1, TaskStatus.NEW);
        manager.addTask(task);
        task.setDescription("Updated Description");
        manager.updateTask(task);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Updated Description", tasks.get(0).getDescription());

        manager = FileBackedTaskManager.loadFromFile(tempFile);
        tasks = manager.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("Updated Description", tasks.get(0).getDescription());
    }
}
