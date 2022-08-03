package Kanban.Service;

import Kanban.Task.Task;

public class Node<E extends Task> {
    public E element;
    public Node<E> next;
    public Node<E> prev;

    public Node(Node<E> prev, E element, Node<E> next) {
        this.element = element;
        this.next = next;
        this.prev = prev;
    }
}
