/* Brandon Rogers
 * masc0325
 * OrderedArrayDictionary.java
 * Last modified: 19 Nov 2014 1405
 * OrderedArrayDictionary is a data structure that orders elements by their
 * key values. It uses generics and can accept any data type as long as the
 * data can be compared using Comparable. It maintains order during insertion.
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedArrayDictionary<K,V> implements DictionaryADT<K,V> {
    private int currentSize, maxSize;
    private long modificationCounter;
    private DictionaryNode<K,V> [] storage;
    
    class DictionaryNode<K,V> {
        K key;
        V value;
        public DictionaryNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    public OrderedArrayDictionary(int whatSize) {
        currentSize = 0; modificationCounter = 0;
        maxSize = whatSize;
        storage = new DictionaryNode[maxSize]; 
    }
// Returns true if the dictionary has an object identified by
// key in it, otherwise false.
    public boolean contains(K key) {
        int isFound = find(key);
        return isFound != -1;
    }

// Adds the given key/value pair to the dictionary.  Returns 
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
    public boolean add(K key, V value) {
        if (isFull()) {
            return false;
        }
        if (contains(key)) {
            return false;
        }
        DictionaryNode<K,V> newNode = new DictionaryNode<K,V>(key, value);
        int insertionPoint = findInsertionPoint(newNode.key);
        //System.out.print("Insertion Point for " + key.toString() + " is " + insertionPoint);
        for (int i = currentSize - 1; i >= insertionPoint; i--)
            storage[i + 1] = storage[i];
        currentSize++;
        modificationCounter++;
        storage[insertionPoint] = newNode;
        return true;
    }

// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
    public boolean delete(K key) {
        if (isEmpty())
            return false;
        int removeIndex = find(key);
        if (removeIndex == -1) {
            return false;
        }
        deleteNode(removeIndex);
        return true;
    }

// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
    public V getValue(K key) {
        int isFound = find(key);
        if (isFound == -1)
            return null;
        return storage[isFound].value;
    }

// Returns the key associated with the parameter value.  Returns
// null if the value is not found in the dictionary.  If more 
// than one key exists that matches the given value, returns the
// first one found. 
    public K getKey(V value) {
        DictionaryNode<K,V> newNode = new DictionaryNode<K,V>(null, value);
        for (int i = 0; i < currentSize; i++) {
            if (((Comparable<V>)newNode.value).compareTo((V)storage[i].value) == 0) {
                return storage[i].key;
            }
        }
        return null;
    }

// Returns the number of key/value pairs currently stored 
// in the dictionary
    public int size() {
        return currentSize;
    }

// Returns true if the dictionary is at max capacity
    public boolean isFull() {
        return currentSize == maxSize;
    }

// Returns true if the dictionary is empty
    public boolean isEmpty() {
        return currentSize == 0;
    }

// Returns the Dictionary object to an empty state.
    public void clear() {
        currentSize = 0;
        modificationCounter = 0;
        storage = new DictionaryNode[maxSize]; 
    }

// Returns an Iterator of the keys in the dictionary, in ascending
// sorted order.  The iterator must be fail-fast.
    public Iterator<K> keys() {
        return new IteratorHelperKeys();
    }

// Returns an Iterator of the values in the dictionary.  The
// order of the values must match the order of the keys. 
// The iterator must be fail-fast. 
    public Iterator<V> values() {
        return new IteratorHelperValues();
    }
    
//  IteratorHelperKeys provides the methods needed to return an interator 
//  over all of the keys of the dictionary. Fails fast
    class IteratorHelperKeys implements Iterator<K> {

        int iterIndex;
        long modCounter;

        public IteratorHelperKeys() {
            iterIndex = 0;
            modCounter = modificationCounter;
        }

        public boolean hasNext() {
            if (modCounter != modificationCounter) 
                throw new ConcurrentModificationException();
            return iterIndex < currentSize;
        }

        public K next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            return storage[iterIndex++].key;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

//  IteratorHelperKeys provides the methods needed to return an interator 
//  over all of the values of the dictionary. Fails fast. Since the dictionary
//  is already in sorted key order, no additional sorting is needed.
    class IteratorHelperValues implements Iterator<V> {

        int iterIndex;
        long modCounter;

        public IteratorHelperValues() {
            iterIndex = 0;
            modCounter = modificationCounter;
        }

        public boolean hasNext() {
            if (modCounter != modificationCounter) 
                throw new ConcurrentModificationException();
            return iterIndex < currentSize;
        }

        public V next() {
            if (!hasNext()) 
                throw new NoSuchElementException();
            return storage[iterIndex++].value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
/*  binSearch implements the binary search algorithm using a temporary mid
*   input:
*   obj: any object type E
*   lo: int corresponding to the lower bound of the vector to be searched
*   hi: int corresponding to the upper bound of the vector
*   output:
    mid: index int in storage[] where object is located
*   -1: object not found
*   recurses until either of these two output conditions are met
*/
    private int binSearch(K key, int lo, int hi) {
        int mid = (lo + hi) / 2;    //midpoint used to compare obj

        if (lo > hi)        //Object not found condition
            return -1;      
                         
        int comparison = ((Comparable<K>)key).compareTo(storage[mid].key);
        if (comparison > 0) {
            return binSearch(key, mid + 1, hi);
        } else if (comparison < 0) {
            return binSearch(key, lo, mid - 1);
        } else {
            return mid;
        }
    }
/*  binSearchInsert implements the same arguments and algorithm
*   as binSearch except that it does not expect to find a number, rather
*   it is used for generic insert methods to find placement location 
*   in the storage array for vector
*   input:
*   obj - any object type E
*   lo - int corresponding to the lower bound of the vector to be searched
*   hi - int corresponding to the upper bound of the vector
*   output: 
*   int lo - the index location to insert
*/
    private int binSearchInsert(K key, int lo, int hi) {
        int mid = (lo + hi) / 2;        //midpoint used to compare obj
        
        if (lo > hi)        // Index location found 
            return lo;      
        int comparison = ((Comparable<K>)key).compareTo(storage[mid].key);
        if (comparison <= 0) {
            return binSearchInsert(key, lo, mid - 1);
        }    
        else
            return binSearchInsert(key, mid + 1, hi);
    }
    
        private int binSearchContains(K key, int lo, int hi) {
        int mid = (lo + hi) / 2;    //midpoint used to compare obj

        if (lo > hi)        //Object not found condition
            return -1;      
                         
        int comparison = ((Comparable<K>) key).compareTo(storage[mid].key);
        if (comparison > 0) {
            return binSearch(key, mid + 1, hi);
        } else if (comparison < 0) {
            return binSearch(key, lo, mid - 1);
        } else {
            return mid;
        }
    }
//  Finds obj E in the array according to binSearch()
    private int find(K key) {
        return binSearch(key, 0, currentSize - 1);
    }

//  Finds an index location to insert an E according to binSearchInsert()
    private int findInsertionPoint(K key) {
        return binSearchInsert(key, 0, currentSize - 1);
    }

//  Deletes an element (int index) from the array and shifts all elements 
//  to the left.
    private void deleteNode(int index) {
        for (int i = index; i < currentSize - 1; i++) 
            storage[i] = storage[i+1];
    }    
}