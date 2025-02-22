import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;
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

    public Duration calculateEpicDuration() {
        return subtasks.stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime calculateEpicStartTime() {
        return subtasks.stream()
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public LocalDateTime calculateEpicEndTime() {
        return subtasks.stream()
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void updateEpicDetails() {
        setDuration(calculateEpicDuration());
        setStartTime(calculateEpicStartTime());
        setEndTime(calculateEpicEndTime());
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
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", subtasks=" + subtasks +
                '}';
    }
}
