package Kanban.Service;

import Kanban.Task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{
    private final Map<Integer, Node<Task>> nodesOfTask = new HashMap<>();
    private final CustomLinkedList<Task> linkTask = new CustomLinkedList<>();

    @Override
    public List<Task> getHistory() {
        return linkTask.getTasks();
    }

    @Override
    public void addHistoryTask(Task task) {
        Node<Task> node = linkTask.linkLast(task);
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

    private class CustomLinkedList<E extends Task> {
        Node<E> first;
        Node<E> last;

        private Node<E> linkLast(E element) {
            Node<E> l = last;
            Node<E> newNode = new Node<>(l, element, null);
            last = newNode;

            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }
            return newNode;
        }

        private void removeNode (Node<E> node) {
            E element = node.element;
            Node<E> prev = node.prev;
            Node<E> next = node.next;

            if (prev == null) {
                first = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                last = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }
            node.element = null;
        }

        List<Task> getTasks() {
            List<Task> taskArrayList = new ArrayList<>();
            for (Node<E> f = first; f != null ; f = f.next) {
                taskArrayList.add(f.element);
            }
            return taskArrayList;
        }
    }
}
