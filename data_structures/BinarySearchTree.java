/*
*   Brandon Rogers
*   BinarySearchTree.java
*   masc0325 CS310
*   This implementation of a BST is a non-balancing tree consisting of 
*   BinaryNode objects which hold key-value pairs.
 */
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author brandonrogers
 */
public class BinarySearchTree<K, V> implements DictionaryADT<K, V> {
    
    protected class BinaryNode<K,V> {
        private K key;              // unique key
        private V value;            // non-unique data
        private BinaryNode<K,V> leftChild;
        private BinaryNode<K,V> rightChild;
        
        public BinaryNode(K key, V value) {
            this.key = key;
            this.value = value;
            leftChild = rightChild = null;
        }
    }
    
    private BinaryNode<K,V> root;
    private int currentSize, modCounter;
    private K foundKey;
    
    public BinarySearchTree() {
        root = null;
        currentSize = 0; modCounter = 0;
    }
    
// Returns true if the dictionary has an object identified by
// key in it, otherwise false.
    public boolean contains(K key) {
        return (find(key, root) != null);
    }

// Adds the given key/value pair to the dictionary.  Returns 
// false if the dictionary is full, or if the key is a duplicate.
// Returns true if addition succeeded.
    public boolean add(K key, V value) {
        if(contains(key))
            return false;
        if(root == null) {
            root = new BinaryNode<K,V>(key, value);
        }
        else {
            insert(key, value, root, null, false);
        }
        currentSize++;
        modCounter++;
        return true;
    }

// Deletes the key/value pair identified by the key parameter.
// Returns true if the key/value pair was found and removed,
// otherwise false.
    public boolean delete(K key) {
        // attempt to call delete on an empty tree
        if (isEmpty())
            return false;
        boolean wasRemoved = remove(key, root, null, false);
        if (!wasRemoved)
            return false;
        currentSize--;
        modCounter++;
        return true;
    }

// Returns the value associated with the parameter key.  Returns
// null if the key is not found or the dictionary is empty.
    public V getValue(K key) {
        if (root == null)           // empty tree
            return null;
        return find(key, root);
    }

// Returns the key associated with the parameter value.  Returns
// null if the value is not found in the dictionary.  If more 
// than one key exists that matches the given value, returns the
// first one found. 
    public K getKey(V value) {
        foundKey = null;
        keyFinder(value, root);
        return foundKey;
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
        root = null;
        currentSize = 0;
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
    
    
    
abstract class IteratorHelper<E> implements Iterator<E> {
        protected BinaryNode<K,V> [] nodes;
        protected int idx, index;
        protected long modificationCounter;
        
        public IteratorHelper() {
            nodes = new BinaryNode[currentSize];
            idx = 0;
            index = 0;
            modificationCounter = modCounter;
            makeOrderedArray(nodes);
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
        
        /////// Begin private methods ///////////
        private void makeOrderedArray(BinaryNode<K,V>[] nodes) {
            inOrder(root);
        }
    
        // inOrder traversal of the array, fills nodes[] in key order
        private void inOrder(BinaryNode<K,V> n) {
            if (n == null) 
                return;
            inOrder(n.leftChild);
            nodes[index++] = n;
            inOrder(n.rightChild);
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
    
    private boolean remove(K key, BinaryNode<K,V> node, BinaryNode<K,V> parent, 
            boolean wasLeft) {
        // key not found!
        if (node == null) 
            return false;
        // comparison is less than 0, go left
        else if (((Comparable<K>)key).compareTo((K)node.key) < 0)
            return remove(key, node.leftChild, node, true);
        // comparison is less than 0, go right (wasLeft = false)
        else if (((Comparable<K>)key).compareTo((K)node.key) > 0)
            return remove(key, node.rightChild, node, false);
        else {
        /////////////////// node found (compareTo == 0) //////////////////////
        // removing the root node and it's the only node
            if (parent == null) {
                if (currentSize == 1) {
                    root = null;
                    return true;
                }           
            }
	    // Create a fake parent because the root needs to be removed
	    BinaryNode<K,V> fakeParent = new BinaryNode(null, null);
	    parent = fakeParent;
	    parent.leftChild = root;
	    parent.rightChild = root;
	    
        ///////////////////// Zero Children ////////////////////////////////
            if (node.leftChild == null && node.rightChild == null) {
                if (wasLeft)
                    parent.leftChild = null;
                else
                    parent.rightChild = null;
                return true;
            }
        ////////////////////// One Child (to rule them all) ///////////////////                     
            // node is on the right side
            if (node.leftChild == null) {
                // update the root
                if (node == root)
                    root = node.rightChild;   
                else {
                    if (wasLeft)
                        parent.leftChild = node.rightChild;               
                    else
                        parent.rightChild = node.rightChild;
                return true;
                }
            }
        
            if (node.rightChild == null) {
                if (node == root)
                    root = node.leftChild;
                else {
                    if (wasLeft)
                        parent.leftChild = node.leftChild;
                    else
                        parent.rightChild = node.leftChild;
                }
            }
       
        //////////////////// Two Children ///////////////////////////////
            else {
                // immediately go right to find the in-order successor
                BinaryNode<K,V> temp = node.rightChild;
                BinaryNode<K,V> otherTemp = null;
                // goes left as far as possible until the left child is null
                while (temp.leftChild != null) {
                    otherTemp = temp;
                    temp = temp.leftChild;
                }
                // temp is one level down from the node to be removed or root
                if (otherTemp == null) 
                    temp = null;
                else
                // temp is two or more levels down
                    otherTemp.leftChild = temp.rightChild;
                if (temp == null) {
                    node.key = node.rightChild.key;
                    node.value = node.rightChild.value;
                    node.rightChild = node.rightChild.rightChild;
                }
                else {
                    node.key = temp.key;
                    node.value = temp.value;
                }
            }
            return true;
        }
    }
    // recursive function to find a value given a unique key
    private V find(K key, BinaryNode<K,V> node) {
        if (node == null)       // not found condition
            return null;
        if (((Comparable<K>)key).compareTo(node.key) < 0)   // go to the left
            return find(key, node.leftChild);
        else if (((Comparable<K>)key).compareTo(node.key) > 0)  // go right
            return find(key, node.rightChild);
        return (V)node.value;                   // found condition
    }
    
    private void keyFinder(V val, BinaryNode<K,V> n) {
        if (n == null)
            return;
        if (((Comparable<V>)val).compareTo((V)n.value) == 0) {
            foundKey = n.key;
            return;
        }
        keyFinder(val, n.rightChild);
        keyFinder(val, n.leftChild);
    }
    
    // inserts a key-value pair into a new Binary node
    private void insert(K key, V value, BinaryNode<K,V> node, 
            BinaryNode<K,V> parent, boolean wasLeft) {
        if(node == null) {          // leaf node detected
            if(wasLeft)
                parent.leftChild = new BinaryNode<K,V>(key, value);
            else
                parent.rightChild = new BinaryNode<K,V>(key, value);
        }
        else if (((Comparable<K>)key).compareTo((K)node.key) < 0)
            insert(key, value, node.leftChild, node, true);
        else
            insert(key, value, node.rightChild, node, false);
    }
}
