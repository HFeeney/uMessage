package p2.sorts;

import datastructures.worklists.MinFourHeap;

import java.util.Comparator;

public class HeapSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        // call build heap on array
        MinFourHeap<E> heap = new MinFourHeap<>(array, comparator);

        int size = heap.size();
        //
        for (int i = 0; i < size; i++) {
            array[i] = heap.next();
        }
    }
}
