package datastructures.worklists;

import cse332.interfaces.worklists.PriorityWorkList;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/worklists/PriorityWorkList.java
 * for method specifications.
 */
public class MinFourHeap<E> extends PriorityWorkList<E> {
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_HEIGHT = 3;
    private static final int DEFAULT_SIZE = (int) (1 - Math.pow(NUM_CHILDREN, DEFAULT_HEIGHT + 1)) / (1 - NUM_CHILDREN);
    /* Do not change the name of this field; the tests rely on it to work correctly. */
    private E[] data;
    private int size;
    private Comparator<E> comparator;

    public MinFourHeap(Comparator<E> c) {
        this.data = (E[]) new Object[DEFAULT_SIZE];
        this.size = 0;
        this.comparator = c;
    }

    /**
     * Initializes a heap with the values in a given array. (uses build heap)
     * @param arr values to be added to the heap
     * @param c comparator used to influence ordering in the heap.
     */
    public MinFourHeap(E[] arr, Comparator<E> c) {
        this.comparator = c;
        this.data = (E[]) new Object[arr.length];
        this.size = arr.length;

        for (int i = arr.length - 1; i >= 0; i--) {
            int insertAt = percolateDown(i, arr[i]);
            data[insertAt] = arr[i];
        }
    }

    @Override
    public boolean hasWork() {
        return this.size > 0;
    }

    @Override
    public void add(E work) {
        if (this.size == this.data.length) {
            resize();
        }
        int insertIndex = percolateUp(this.size, work);
        this.size++;
        data[insertIndex] = work;
    }

    @Override
    public E peek() {
        if (!hasWork()) throw new NoSuchElementException();
        return data[0]; // the minimum data is at the root node
    }

    @Override
    public E next() {
        if (!hasWork()) throw new NoSuchElementException();
        E result = peek();
        size--;
        E removedNode = data[size];
        int insertIndex = percolateDown(0, removedNode);
        data[insertIndex] = removedNode;
        return result;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.data = (E[]) new Object[DEFAULT_SIZE];
        this.size = 0;
    }

    /**
     * Resizes the existing data array by doubling its size.
     */
    private void resize() {
        E[] newData = (E[]) new Object[this.data.length * 2];
        for (int i = 0; i < this.data.length; i++) {
            newData[i] = this.data[i];
        }
        this.data = newData;
    }

    /**
     * Finds a proper index in the heap for a value, given where the newest node in the heap is.
     * Adjusts existing values to make room for the new one.
     * @param spot the index of the newest node in the heap
     * @param value the value whose proper insertion index will be found
     * @return the insertion index of the new value based on the heap ordering
     */
    private int percolateUp(int spot, E value) {
        // as long as the spot is not the root and the value is less than the current node's parent's,
        // advance spot up a level by swapping with the parent
        while (spot > 0 && comparator.compare(value, this.data[(spot - 1) / NUM_CHILDREN]) < 0) {
            this.data[spot] = this.data[(spot - 1) / NUM_CHILDREN];
            spot = (spot - 1) / NUM_CHILDREN;
        }
        return spot;
    }

    /**
     * Finds a proper index in the heap for a value given a starting node.
     * Adjusts existing values to make a spot for the new one.
     * @param spot the index of the newest node in the heap
     * @param value the value whose proper insertion index will be found
     * @return the insertion index of the new value based on the heap ordering
     */
    private int percolateDown(int spot, E value) {
        // as long as the spot is not at a leaf and has a smaller child,
        // advance spot down a level by swapping with the smallest child
        while (size > spot * NUM_CHILDREN + 1) {
            // assume the smallest child is child 0, then find the smallest child
            int indexOfSmallest = spot * NUM_CHILDREN + 1;
            for (int i = 1; i < NUM_CHILDREN && spot * NUM_CHILDREN + i + 1 < size; i++) {
                if (comparator.compare(data[spot * NUM_CHILDREN + i + 1], data[indexOfSmallest]) < 0) {
                    indexOfSmallest = spot * NUM_CHILDREN + i + 1;
                }
            }
            if (comparator.compare(data[indexOfSmallest], value) < 0) {
                // swap data at spot with the smallest child's data
                data[spot] = data[indexOfSmallest];
                spot = indexOfSmallest;
            } else {
                // the children were all larger so exit
                break;
            }
        }
        return spot;
    }
}
