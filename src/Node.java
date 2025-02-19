import java.util.Objects;

class Node {
    int id;
    Task task;
    Node next;
    Node prev;

    Node(int id, Task task) {
        this.id = id;
        this.task = task;
        this.next = null;
        this.prev = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(task);
    }
}
