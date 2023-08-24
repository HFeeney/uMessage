package p2.sorts;

import java.util.Comparator;

public class QuickSort {
    public static <E extends Comparable<E>> void sort(E[] array) {
        QuickSort.sort(array, (x, y) -> x.compareTo(y));
    }

    public static <E> void sort(E[] array, Comparator<E> comparator) {
        sort(array, comparator, 0, array.length - 1);
    }

    private static <E> void sort(E[] array, Comparator<E> c, int lo, int hi) {
        if (lo < hi) {
            // partition the given array, store index of pivot
            int pivotIndex = partition(array, c, lo, hi);

            sort(array, c, lo, pivotIndex - 1);
            sort(array, c, pivotIndex + 1, hi);
        }
    }

    // return index of pivot
    private static <E> int partition(E[] array, Comparator<E> c, int lo, int hi) {
        // pick pivot
        E pivot = array[hi];

        // index of last element smaller than pivot
        int i = lo - 1;

        // j is index of element of question, start 1 element ahead of i
        for (int j = lo; j < hi; j++) {
            // place this element on left side if it is less than the pivot
            if (c.compare(array[j], pivot) < 0) {
                // put the small value directly right of the last one, update index of last small element
                i++;
                swap(array, i, j);
            }
        }

        // put the pivot directly right of last thing smaller than it
        swap(array, i + 1, hi);
        // return the index of the pivot
        return i + 1;
    }

    private static <E> void swap(E[] array, int a, int b) {
        E temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }
}
