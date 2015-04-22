/*
*   Brandon Rogers
*   HashTable.java
*   masc0325 CS310
*   HashTable.java is a HashTable implementation of a dictionary. It stores
*   key-value pairs inside of DictionaryNodes, a wrapper class.
*   Guaranteed performance for key lookups is O(1)
 */
package data_structures;

import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Wrapper class DictionaryNode contains the data elements used by the Hash
 * Table.
 */
public class HashTable<K,V> implements DictionaryADT<K,V> {
    private int currentSize, tableSize, maxSize;
    private long modCounter;
    private UnorderedList<DictionaryNode<K,V>> [] list;
    
    private class DictionaryNode<K,V> implements Comparable<DictionaryNode<K,V>>
    {
        K key;
        V value;
        public DictionaryNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
        public int compareTo(DictionaryNode<K,V> node) {
            return ((Comparable<K>)key).compareTo((K)node.key);
        }
    }
    
    public HashTable(int maxSize) {
        currentSize = 0;
        this.maxSize = maxSize;
        tableSize = (maxSize * 9) / 10;
        list = new UnorderedList[maxSize];
        for (int i = 0; i < tableSize; i++)
            list[i] = new UnorderedList<DictionaryNode<K,V>>();
    }
// Returns true if the dictionary has an object identified by
// key in it, otherwise false.
    public boolean contains(K key) {
        int hashIndex = getIndex(key);
        for (DictionaryNode<K,V> node : list[hashIndex]) {
            if (((Comparable<K>)node.key).compareTo(key) == 0)
                return true;
        }
        return false;
    }

// Adds the given key/value pair to the dictionary.  Returns 
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
    public boolean add(K key, V value) {
        if (contains(key))
            return false;
        int hashIndex = getIndex(key);
        DictionaryNode<K,V> newNode = new DictionaryNode(key, value);
            if (list[hashIndex] == null) {
                list[hashIndex].insertFirst(newNode);
            }
            else {
                list[hashIndex].insertLast(newNode);
            }
        currentSize++;
        modCounter++;
        return true;
    }

// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
    public boolean delete(K key) {
        int hashIndex = getIndex(key);
        DictionaryNode<K,V> newNode = new DictionaryNode(key, null);
            if (list[hashIndex] == null) {
                return false;
            }
        currentSize--;
        modCounter++;
        return list[hashIndex].remove(newNode) != null;
    }

// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
    public V getValue(K key) {
        int hashIndex = getIndex(key);
        DictionaryNode<K,V> newNode = new DictionaryNode(key, "Key doesn't exist"
                + " in this dictionary");
        if (list[hashIndex] == null)
            return null;
        else
            for (DictionaryNode<K,V> iterNode : list[hashIndex]) {
                if (newNode.compareTo(iterNode) == 0)
                    return iterNode.value;
            }
        return null;
    }

// Returns the key associated with the parameter value.  Returns
// null if the value is not found in the dictionary.  If more 
// than one key exists that matches the given value, returns the
// first one found. 
    public K getKey(V value) {
        DictionaryNode<K,V> newNode = new DictionaryNode(null, value);

        for (int i = 0; i < tableSize; i++) {
            for (DictionaryNode<K,V> node : list[i])
                if (((Comparable<V>)newNode.value).compareTo((V)node.value) == 0)
                    return node.key;
            }
        // object not found
        return null;
    }

// Returns the number of key/value pairs currently stored 
// in the dictionary
    public int size() {
        return currentSize;
    }

// Returns true if the dictionary is at max capacity
    public boolean isFull() {
        return false;
    }

// Returns true if the dictionary is empty
    public boolean isEmpty() {
        return currentSize == 0;
    }

// Returns the Dictionary object to an empty state.
    public void clear() {
    list = new UnorderedList[maxSize];
        for (int i = 0; i < maxSize; i++)
            list[i] = new UnorderedList<DictionaryNode<K,V>>();
    currentSize = 0;
    modCounter = 0;
    }

    
    abstract class IteratorHelper<E> implements Iterator<E> {
        protected DictionaryNode<K,V> [] nodes;
        protected int idx;
        protected long modificationCounter;
        
        public IteratorHelper() {
            nodes = new DictionaryNode[currentSize];
            idx = 0;
            int j = 0;
            modificationCounter = modCounter;
            for (int i = 0; i < tableSize; i++) {
                for (DictionaryNode node : list[i])
                    nodes[j++] = node;            
            }
            nodes = (DictionaryNode<K,V>[]) shellSort(nodes);
        }
        
        public boolean hasNext() {
            if (modificationCounter != modCounter)
                throw new ConcurrentModificationException();
            return idx < currentSize;
        }
        
        public abstract E next();
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    class KeyIteratorHelper<K> extends IteratorHelper<K> {
        public KeyIteratorHelper() {
            super();
        }
        
        public K next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            return (K) nodes[idx++].key;
        }
    }
    
    class ValueIteratorHelper<V> extends IteratorHelper<V> {
        public ValueIteratorHelper() {
            super();
        }
        
        public V next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            return (V) nodes[idx++].value;
        }
    }
    
// Returns an Iterator of the keys in the dictionary, in ascending
// sorted order.  The iterator must be fail-fast.
    public Iterator<K> keys() {
        return new KeyIteratorHelper();
    }


// Returns an Iterator of the values in the dictionary.  The
// order of the values must match the order of the keys. 
// The iterator must be fail-fast. 
    public Iterator<V> values() {
        return new ValueIteratorHelper();
    }
    
    private int getIndex(K key) {
        return (key.hashCode() & 0x7FFFFFFF ) % tableSize;
    }
    
    
    private DictionaryNode<K,V>[] shellSort(DictionaryNode<K,V>[] toSort) {
        DictionaryNode<K,V>[] nodeArray = toSort;
        DictionaryNode<K,V> temp;
        int in, out, h = 1;
        int size = nodeArray.length;
        
        while (h <= size / 3)       // h calculates gaps between jumps
            h = h * 3 + 1;
        while (h > 0) {
            for (out = h; out < size; out++) {
                temp = nodeArray[out];
                in = out;
                while (in > h-1 && nodeArray[in - h].compareTo(temp) >= 0) {
                    nodeArray[in] = nodeArray[in - h];
                    in -= h;
                }
            nodeArray[in] = temp;    
            }   // end for loop
        h = (h-1) / 3;
        }
        return nodeArray;
    }
}
