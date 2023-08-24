package datastructures.worklists;

import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.worklists.FixedSizeFIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/FixedSizeFIFOWorkList.java
 * for method specifications.
 */
public class CircularArrayFIFOQueue<E extends Comparable> extends FixedSizeFIFOWorkList<E> {
    private int size;
    private E[] arr;
    private int head;
    private int back;
    public CircularArrayFIFOQueue(int capacity) {
        super(capacity);
        size = 0;
        arr = createGenericArray(capacity);
        head = 0;
        back = 0;
    }

    @Override
    public void add(E work) {
        if (isFull()) throw new IllegalStateException();
        arr[back] = work;
        back = (back + 1) % capacity();
        size++;
    }

    @Override
    public E peek() {
        if (!hasWork()) throw new NoSuchElementException();
        return arr[head];
    }

    @Override
    public E peek(int i) {
        if (!hasWork()) throw new NoSuchElementException();
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
        int index = (head + i) % capacity();
        return arr[index];
    }

    @Override
    public E next() {
        if (!hasWork()) throw new NoSuchElementException();
        E res = arr[head];
        head = (head + 1) % capacity();
        size--;
        return res;
    }

    @Override
    public void update(int i, E value) {
        if (!hasWork()) throw new NoSuchElementException();
        if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
        int index = (head + i) % capacity();
        arr[index] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        arr = createGenericArray(capacity());
        head = 0;
        back = 0;
    }

    @Override
    public int compareTo(FixedSizeFIFOWorkList<E> other) {
        // You will implement this method in project 2. Leave this method unchanged for project 1.
        int result = 0;
        for (int i = 0; i < Math.min(this.size(), other.size()); i++) {
            E thisElement = this.next();
            E otherElement = other.next();
            this.add(thisElement);
            other.add(otherElement);
            if (result == 0) {
                result = thisElement.compareTo(otherElement);
            }
        }
        if (this.size > other.size()) {
            for (int i = 0; i < this.size() - other.size(); i++) {
                this.add(this.next());
            }
        }
        if (this.size < other.size()) {
            for (int i = 0; i < other.size() - this.size(); i++) {
                other.add(other.next());
            }
        }
        if (this.size() != other.size() && result == 0) {
            result = this.size() - other.size();
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        // You will finish implementing this method in project 2. Leave this method unchanged for project 1.
        if (this == obj) {
            return true;
        } else if (!(obj instanceof FixedSizeFIFOWorkList<?>)) {
            return false;
        } else {
            // Uncomment the line below for p2 when you implement equals
             FixedSizeFIFOWorkList<E> other = (FixedSizeFIFOWorkList<E>) obj;
             if (size != other.size()) return false;
             boolean equal = true;
             for (int i = 0; i < size; i++) {
                 E thisElement = this.next();
                 E otherElement = other.next();
                 if (!thisElement.equals(otherElement)) {
                    equal = false;
                 }
                 this.add(thisElement);
                 other.add(otherElement);
             }
             return equal;
        }
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(this.size);
        for (int i = 0; i < this.size; i++) {
            result = 31 * result + this.arr[(this.head + i) % capacity()].hashCode();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private E[] createGenericArray(int length) {
        return (E[])new Comparable[length];
    }
}
