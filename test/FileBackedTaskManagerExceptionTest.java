import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerExceptionTest {

    @Test
    public void testLoadFromFileWithIOException() {
        File tempFile = new File("non_existent_file.csv");
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(tempFile);
        }, "Загрузка из несуществующего файла должна вызвать ManagerSaveException");
    }

    @Test
    public void testSaveToFileWithIOException() throws IOException {
        File tempFile = Files.createTempFile("tasks", ".csv").toFile();
        tempFile.setReadOnly();

        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());

        assertThrows(ManagerSaveException.class, () -> {
            manager.addTask(task);
        }, "Сохранение в файл с атрибутом только для чтения должно вызвать ManagerSaveException");

        tempFile.setWritable(true);
        tempFile.delete();
    }

    @Test
    public void testSaveToFileWithoutIOException() throws IOException {
        File tempFile = Files.createTempFile("tasks", ".csv").toFile();

        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        Task task = new Task("Task 1", "Description 1", 1, TaskStatus.NEW, Duration.ofMinutes(60), LocalDateTime.now());

        assertDoesNotThrow(() -> {
            manager.addTask(task);
        }, "Сохранение задачи в нормальный файл не должно вызывать исключений");

        tempFile.delete();
    }
}
