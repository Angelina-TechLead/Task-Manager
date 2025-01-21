import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Task> tasks;
    private Map<Integer, Node> nodeMap;
    private Node head;
    private Node tail;
    private int idCounter;

    public InMemoryHistoryManager() {
        tasks = new HashMap<>();
        nodeMap = new HashMap<>();
        head = null;
        tail = null;
        idCounter = 1;
    }

    @Override
    public void add(Task task) {
        task.setId(idCounter++);
        tasks.put(task.getId(), task);
        Node newNode = new Node(task.getId(), task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        if (tasks.containsKey(id)) {
            Node nodeToRemove = nodeMap.get(id);
            if (nodeToRemove != null) {
                if (nodeToRemove == head) {
                    head = head.next;
                    if (head != null) {
                        head.prev = null;
                    }
                } else if (nodeToRemove == tail) {
                    tail = tail.prev;
                    if (tail != null) {
                        tail.next = null;
                    }
                } else {
                    nodeToRemove.prev.next = nodeToRemove.next;
                    nodeToRemove.next.prev = nodeToRemove.prev;
                }
                nodeMap.remove(id);
                tasks.remove(id);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        Node current = head;
        while (current != null) {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }

    List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node current = head;
        while (current != null) {
            taskList.add(current.task);
            current = current.next;
        }
        return taskList;
    }
}
