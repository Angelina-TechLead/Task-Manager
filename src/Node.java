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
}
