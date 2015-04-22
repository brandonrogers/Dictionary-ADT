/*
*   Brandon Rogers
*   RedBlackTree.java
*   masc0325 CS310
*   RedBlackTree.java implements the DictionaryADT and uses the Java API 
*   TreeMap to hold key-value entries in a balanced Red/Black tree.
*   Guaranteed lookup performance is O log(n)
 */
package data_structures;

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author brandonrogers
 */
public class RedBlackTree<K,V> implements DictionaryADT<K,V> {
    TreeMap dicTree;
    
    public RedBlackTree() {
        dicTree = new TreeMap();
    }
// Returns true if the dictionary has an object identified by
// key in it, otherwise false.
    public boolean contains(K key) {
        return dicTree.containsKey(key);
    }

// Adds the given key/value pair to the dictionary.  Returns 
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
    public boolean add(K key, V value) {
        if(contains(key))
            return false;
        dicTree.put((K)key, (V)value);
        return true;
    }

// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
    public boolean delete(K key) {
        V temp = (V)dicTree.remove(key);
        return temp != null;
    }

// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
    public V getValue(K key) {
        return (V)dicTree.get((K)key);
    }

// Returns the key associated with the parameter value.  Returns
// null if the value is not found in the dictionary.  If more 
// than one key exists that matches the given value, returns the
// first one found. 
    public K getKey(V value) {

        V tempVal; K tempKey;
        Iterator keyIter = keys();
        Iterator valIter = values();
        while (valIter.hasNext()){
            tempKey = (K)keyIter.next();
            tempVal = (V)valIter.next();
            if (((Comparable<V>)tempVal).compareTo((V)value) == 0)
                return tempKey;
        }
        return null;
    }

// Returns the number of key/value pairs currently stored 
// in the dictionary
    public int size() {
        return dicTree.size();
    }

// Returns true if the dictionary is at max capacity
    public boolean isFull() {
        return false;
    }

// Returns true if the dictionary is empty
    public boolean isEmpty() {
        return dicTree.size() == 0;
    }

// Returns the Dictionary object to an empty state.
    public void clear() {
        dicTree.clear();
    }

// Returns an Iterator of the keys in the dictionary, in ascending
// sorted order.  The iterator must be fail-fast.
    public Iterator<K> keys() {
        return dicTree.keySet().iterator();
    }

// Returns an Iterator of the values in the dictionary.  The
// order of the values must match the order of the keys. 
// The iterator must be fail-fast. 
    public Iterator<V> values() {
        return dicTree.values().iterator();
    }
}
