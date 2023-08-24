package p2.sorts;

import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class TopKSort {
    public static <E extends Comparable<E>> void sort(E[] array, int k) {
        sort(array, k, (x, y) -> x.compareTo(y));
    }

    /**
     * Behaviour is undefined when k > array.length
     */
    public static <E> void sort(E[] array, int k, Comparator<E> comparator) {
        // put k things in a heap
        MinFourHeap<E> heap = new MinFourHeap<>(comparator);
        for (int i = 0; i < Math.min(k, array.length); i++) {
            heap.add(array[i]);
        }
        // add the rest with decision making
        for (int i = k; i < array.length; i++) {
            // if top is less than next item to insert, delete it and replace
            if (comparator.compare(heap.peek(), array[i]) < 0) {
                heap.next();
                heap.add(array[i]);
            }
        }
        // now delete all the shit and put it into the array in order, make the rest of the array null
        for (int i = 0; i < array.length; i++) {
            array[i] = i < k ? heap.next() : null;
        }
    }
}
