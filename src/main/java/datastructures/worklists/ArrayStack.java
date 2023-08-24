package datastructures.worklists;

import cse332.interfaces.worklists.LIFOWorkList;

import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/LIFOWorkList.java
 * for method specifications.
 */
public class ArrayStack<E> extends LIFOWorkList<E> {

    private E[] arr;
    private int size;

    public ArrayStack() {
        arr = createGenericArray(10);
        size = 0;
    }

    @Override
    public void add(E work) {
        if (size == arr.length) {
            // array is full, so copy data over to new array of double length
            E[] newArr = createGenericArray(arr.length * 2);
            for (int i = 0; i < arr.length; i++) {
                newArr[i] = arr[i];
            }
            arr = newArr;
        }
        arr[size] = work;
        size++;
    }

    @Override
    public E peek() {
        if (!hasWork()) throw new NoSuchElementException();
        return arr[size - 1];
    }

    @Override
    public E next() {
        if (!hasWork()) throw new NoSuchElementException();
        E res = arr[size - 1];
        size--;
        return res;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        arr = createGenericArray(10);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private E[] createGenericArray(int length) {
        return (E[])new Object[length];
    }
}
