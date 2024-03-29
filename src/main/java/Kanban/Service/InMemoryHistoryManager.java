package Kanban.Service;

import Kanban.Task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node<Task>> nodesOfTask = new HashMap<>();
    private final CustomLinkedList<Task> linkTask = new CustomLinkedList<>();

    @Override
    public List<Task> getHistory() {
        return linkTask.getTasks();
    }

    @Override
    public void addHistoryTask(Task task) {
        Node<Task> node = null;
        if (task != null) {
            node = linkTask.linkLast(task);
        }
        if (nodesOfTask.containsKey(task.getId())) {
            linkTask.removeNode(nodesOfTask.get(task.getId()));
            nodesOfTask.put(task.getId(), node);
        } else {
            nodesOfTask.put(task.getId(), node);
        }
    }

    @Override
    public void remove(int id) {
        if (nodesOfTask.containsKey(id)) {
            linkTask.removeNode(nodesOfTask.get(id));
            nodesOfTask.remove(id);
        }
    }

    private static class Node<E extends Task> {
        public E element;
        public Node<E> next;
        public Node<E> prev;

        private Node(Node<E> prev, E element, Node<E> next) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(element, node.element);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, next, prev);
        }
    }

    private static class CustomLinkedList<E extends Task> {
        private Node<E> first;
        private Node<E> last;
        private int size = 0;

        private Node<E> linkLast(E element) {
            if (size == 0) {
                first = new Node<>(null, element, null);
                last = first;
            } else {
                Node<E> l = last;
                last = new Node<>(l, element, null);
                l.next = last;
            }

            size++;
            return last;
        }

        private void removeNode (Node<E> node) {
            Node<E> prev = node.prev;
            Node<E> next = node.next;

            if (prev == null) {
                first = next;
                if (next != null) {
                    next.prev = null;
                }
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                last = prev;
                if (last != null) {
                    last.next = null;
                }
            } else {
                next.prev = prev;
                node.next = null;
            }
            size--;
        }

        private List<Task> getTasks() {
            List<Task> taskArrayList = new ArrayList<>();
                Node<E> f = first;
                while (f != null) {
                    taskArrayList.add(f.element);
                    f = f.next;
                }
            return taskArrayList;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(nodesOfTask, that.nodesOfTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodesOfTask, linkTask);
    }
}
