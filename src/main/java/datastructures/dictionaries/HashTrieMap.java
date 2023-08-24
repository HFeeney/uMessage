package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;
import datastructures.worklists.ArrayStack;
import datastructures.worklists.ListFIFOQueue;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    public class HashTrieNode extends TrieNode<ChainingHashTable<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            this.pointers = new ChainingHashTable<A, HashTrieNode>(MoveToFrontList::new);
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieNode>> iterator() {
            ListFIFOQueue<Entry<A, HashTrieNode>> entrySet = new ListFIFOQueue<>();
            for (Item<A, HashTrieNode> item : pointers) {
                entrySet.add(new SimpleEntry<>(item.key, item.value));
            }
            return entrySet.iterator();
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
        this.size = 0;
    }

    @Override
    public V insert(K key, V value) {
        // throw exception if either param is null
        if (key == null || value == null) throw new IllegalArgumentException();

        // currNode initially points to root
        HashTrieNode currNode = (HashTrieNode) this.root;

        // advance currNode along path corresponding to each character in key
        for (A character : key) {
            // if no path with this character leads out of this node,
            // create a node with a path leading to it labeled with this character
            if (currNode.pointers.find(character) == null) {
                currNode.pointers.insert(character, new HashTrieNode());
            }

            // advance currNode along path corresponding to this character
            currNode = currNode.pointers.find(character);
        }

        // will be null if this mapping did not previously exist
        V prevValue = currNode.value;
        currNode.value = value;
        // size only increases if this is a new element
        if (prevValue == null) {
            this.size++;
        }
        return prevValue;
    }

    @Override
    public V find(K key) {
        if (key == null) throw new IllegalArgumentException();

        HashTrieNode currNode = (HashTrieNode) this.root;

        for (A character : key) {
            if (currNode.pointers.find(character) == null) return null;

            currNode = currNode.pointers.find(character);
        }

        return currNode.value;
    }

    @Override
    public boolean findPrefix(K key) {
        if (key == null) throw new IllegalArgumentException();

        HashTrieNode currNode = (HashTrieNode) this.root;

        // if we can follow the entire path traced by the characters of this string, then prefix exists
        for (A character : key) {
            if (currNode.pointers.find(character) == null) return false;
            currNode = currNode.pointers.find(character);
        }

        return true;
    }

    @Override
    public void delete(K key) {
        throw new UnsupportedOperationException();
//        if (key == null) throw new IllegalArgumentException();
//
//        // keep stack of nodes visited and chars since last node that doesn't need to be pruned
//        // starts with node that doesn't need to be pruned
//        ArrayStack<HashTrieNode> visited = new ArrayStack<>();
//        ArrayStack<A> characters = new ArrayStack<>();
//
//        HashTrieNode currNode = (HashTrieNode) this.root;
//        // traverse trie down to last char in key if possible (exit if mapping doesn't exist)
//        for (A character : key) {
//            if (!currNode.pointers.containsKey(character)) return;
//            // restart stacks from this most recent node that doesn't need to be pruned
//            if (currNode.pointers.size() > 1 || currNode.value != null) {
//                visited.clear();
//                characters.clear();
//            }
//            // push nodes and characters onto stack
//            visited.add(currNode);
//            characters.add(character);
//            currNode = currNode.pointers.get(character);
//        }
//
//        // if this node held a value, then size will decrease by 1
//        if (currNode.value != null) {
//            size--;
//        }
//
//        // set value = null
//        currNode.value = null;
//
//        // delete children of all nodes in stack since last node that doesn't need to be pruned
//        // using the characters corresponding to their single child
//        A currCharacter;
//        while (currNode.pointers.size() == 0 && visited.hasWork()) {
//            // pop previous node, character off stack
//            currNode = visited.next();
//            currCharacter = characters.next();
//            // remove mapping for this character from previous node
//            currNode.pointers.remove(currCharacter);
//        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();

//        this.root = new HashTrieNode();
//        this.size = 0;
    }
}