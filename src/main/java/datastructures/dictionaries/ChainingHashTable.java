package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;

import java.util.Iterator;
import java.util.function.Supplier;

/**
 * 1. You must implement a generic chaining hashtable. You may not
 * restrict the size of the input domain (i.e., it must accept
 * any key) or the number of inputs (i.e., it must grow as necessary).
 * 3. Your HashTable should rehash as appropriate (use load factor as
 * shown in class!).
 * 5. HashTable should be able to resize its capacity to prime numbers for more
 * than 200,000 elements. After more than 200,000 elements, it should
 * continue to resize using some other mechanism.
 * 6. You should use the prime numbers in the given PRIME_SIZES list to resize
 * your HashTable with prime numbers.
 * 7. When implementing your iterator, you should NOT copy every item to another
 * dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {
    private Supplier<Dictionary<K, V>> newChain;

    static final int[] PRIME_SIZES =
            {11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197573, 395147};

    private static final double loadLimit = 1.0;
    private int primeIndex;
    private Dictionary<K, V>[] table;

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        primeIndex = 0;
        table = new Dictionary[PRIME_SIZES[primeIndex]];
    }

    @Override
    public V insert(K key, V value) {
        // apply hash function to key, mod by table size to get index
        int index = hashToTableIndex(key, table.length);

        // create a bucket if needed
        if (table[index] == null)
            table[index] = newChain.get();

        // insert the value at the proper bucket and return previously associated value
        V previous = table[index].insert(key, value);

        // if there was no previously associated value, the size increases
        if (previous == null)
            size++;

        // rehash if load factor exceeds limit
        if ((double) size / table.length > loadLimit) {
            // create new table with next prime or double size + 1 if no primes left
            primeIndex++;
            int newTableSize = primeIndex < PRIME_SIZES.length ? PRIME_SIZES[primeIndex] : table.length * 2 + 1;
            Dictionary<K, V>[] newTable = new Dictionary[newTableSize];

            // rehash all values
            for (Item<K, V> i : this) {
                // apply hash function to key, mod by table size to get index
                index = hashToTableIndex(i.key, newTable.length);

                // create a bucket if needed
                if (newTable[index] == null)
                    newTable[index] = newChain.get();

                // insert the value at the proper bucket
                newTable[index].insert(i.key, i.value);
            }

            // reassign table to newTable
            table = newTable;
        }

        // return value previously associated with this key
        return previous;
    }

    @Override
    public V find(K key) {
        // apply hash function to key, mod by table size to get index
        int index = hashToTableIndex(key, table.length);

        // ensure that bucket isn't null
        if (table[index] == null)
            return null;

        // search in dictionary at that index for the key
        return table[index].find(key);
    }

    private int hashToTableIndex(K key, int tableSize) {
        int hashCode = key.hashCode();
        hashCode = hashCode < 0 ? hashCode + Integer.MAX_VALUE + 1 : hashCode;
        return hashCode % tableSize;
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTableIterator();
    }

    private class ChainingHashTableIterator extends SimpleIterator<Item<K, V>> {
        private int bucket = 0;
        private Iterator<Item<K, V>> currentBucketIterator;

        public ChainingHashTableIterator() {
            while (bucket < table.length && table[bucket] == null) {
                bucket++;
            }
            if (bucket < table.length) {
                currentBucketIterator = table[bucket].iterator();
            }
        }

        @Override
        public Item<K, V> next() {
            return currentBucketIterator.next();
        }

        @Override
        public boolean hasNext() {
            // if there is no iterator, there must be no more items
            if (currentBucketIterator == null) return false;

            // if the current bucket has items still, then there are more
            if (currentBucketIterator.hasNext())
                return true;

            // move to the next bucket, continue moving until past end of table or bucket isn't empty
            do {
                bucket++;
            } while (bucket < table.length && table[bucket] == null);

            // if we move past the end of table, then no more items
            if (bucket == table.length)
                return false;

            // update iterator to this bucket's iterator, then return whether it has an item
            currentBucketIterator = table[bucket].iterator();
            return currentBucketIterator.hasNext();
        }
    }
}
