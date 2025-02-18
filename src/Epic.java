import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Epic extends Task {
    private List<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, int id, TaskStatus status) {
        super(name, description, id, status, Duration.ZERO, null);
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateEpicDetails();
    }

    private void updateEpicDetails() {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;
        for (Subtask subtask : subtasks) {
            duration = duration.plus(subtask.getDuration());
            if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasks=" + subtasks +
                '}';
    }
}
