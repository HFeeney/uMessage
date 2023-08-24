package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;

import java.util.Iterator;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {

    protected class Node extends Item<K, V> {
        public Node next;

        public Node(K key, V value) {
            this(key, value, null);
        }

        public Node(K key, V value, Node next) {
            super(key, value);
            this.next = next;
        }
    }

    private class NodeIterator extends SimpleIterator<Item<K, V>> {
        private Node current;

        public NodeIterator() {
            current = head;
        }

        @Override
        public Item<K, V> next() {
            Node result = current;
            current = current.next;
            return result;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }
    }

    private Node head;

    @Override
    public V insert(K key, V value) {
        if (head == null) {
            size++;
            head = new Node(key, value);
            return null;
        }

        Node current = head;

        if (current.key.equals(key)) {
            V prevValue = current.value;
            current.value = value;
            return prevValue;
        }

        while (current.next != null && !current.next.key.equals(key)) {
            current = current.next;
        }

        if (current.next == null) {
            size++;
            head = new Node(key, value, head);
            return null;
        }

        Node temp = current.next;
        current.next = current.next.next;
        temp.next = head;
        head = temp;

        V prevValue = head.value;
        head.value = value;
        return prevValue;
    }

    @Override
    public V find(K key) {
        Node current = head;
        Node previous = null;
        while (current != null && !current.key.equals(key)) {
            previous = current;
            current = current.next;
        }
        if (current == null) {
            return null;
        }
        if (previous != null) {
            previous.next = previous.next.next;
            current.next = head;
            head = current;
        }
        return head.value;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new NodeIterator();
    }
}
