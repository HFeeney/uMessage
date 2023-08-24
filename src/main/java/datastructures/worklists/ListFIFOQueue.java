package datastructures.worklists;

import cse332.interfaces.worklists.FIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FIFOWorkList.java
 * for method specifications.
 */
public class ListFIFOQueue<E> extends FIFOWorkList<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        E data;
        Node<E> next;

        public Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    public ListFIFOQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void add(E work) {
        if (!hasWork()) {
            head = new Node<>(work);
            tail = head;
            size = 1;
        } else {
            tail.next = new Node<>(work);
            tail = tail.next;
            size++;
        }
    }

    @Override
    public E peek() {
        if (!hasWork()) throw new NoSuchElementException();
        return head.data;
    }

    @Override
    public E next() {
        if (!hasWork()) throw new NoSuchElementException();
        E res = head.data;
        head = head.next;
        size--;
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
}
