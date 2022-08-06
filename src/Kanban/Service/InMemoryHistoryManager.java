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
        private Node<E> first;
        private Node<E> last;
        //private int size = 0;

        private Node<E> linkLast(E element) {
            Node<E> l = last;
            Node<E> newNode = new Node<>(l, element, null);
            last = newNode;

            /*if (size == 0) {
                first = null;
                last = null;
            } else if (size == 1) {
                first = newNode;
                last = newNode;
            } else {
                last = newNode;
                l.next = newNode;
            }*/
            //size ++;
            //return newNode;
            if (l == null) {
                first = newNode;
            } else {
                l.next = newNode;
            }
            return newNode;
        }

        private void removeNode (Node<E> node) {
            Node<E> prev = node.prev;
            Node<E> next = node.next;

            if (prev == null) {
                first = next;
                //next.prev = null;
                /*при добавлении предыдущей строки, в случае если в списке один элемент бросается исключение
                  из-за того что следующий элемент next == null, при обращении к его полю next.prev
                  возникает исключение*/
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
            //size--;
        }

        private List<Task> getTasks() {
            List<Task> taskArrayList = new ArrayList<>();
            //for (Node<E> f = first; f != null ; f = f.next) {
                Node<E> f = first;
                while (f != null) {
                    taskArrayList.add(f.element);
                    f = f.next;
                }
            //}
            return taskArrayList;
        }
    }
}
