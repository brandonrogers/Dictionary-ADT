/*  Brandon Rogers
*   masc0325
*   UnorderedList.java 
*   LAST UPDATE 16 OCT 2014 1341
*   Unordered List is an Unordered Linked List class that provides support for 
*   normal unordered Stacks and Queues. It can remove or insert at the head or 
*   tail of the list and also supports a fail-fast Iterator.
*/
package data_structures;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class UnorderedList<E> implements Iterable<E>{
    
    protected class Node<T> {
        T data;
        Node<T> next;
        
        public Node(T obj) {
            data = obj;
            next = null;
        }
    }
    
    private Node head, tail;
    private int currentSize;
    private long modificationCounter;
    
    public UnorderedList() {
        currentSize = 0;
        modificationCounter = 0;
        head = tail = null;
    }
    //Inserts an element at the head. Insertion never fails in a linked list
    public boolean insertFirst(E obj) {
        Node <E>newNode = new Node<E>(obj);
        newNode.next = head;
        head = newNode;
        currentSize++;
        modificationCounter++;
        return true;
    }
    //If list empty the element is inserted at the head = tail
    //otherwise element inserted at the tail and becomes the new tail
    public boolean insertLast(E obj) {
        Node <E>newNode = new Node<E>(obj);
        if (head == null) {
            head = tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }
        currentSize++;
        modificationCounter++;
        return true;
    }
    //Removes the head pointer if it exists and increments the head to the 
    //next element in the linked list
    public E removeFirst() {
        if (isEmpty())              // Empty list, can't remove
            return null;
        E temp = (E)head.data;
        if (head == tail) {
            head = tail = null;     // One element in the list
        }
        else { 
            head = head.next;
        }
        currentSize--;
        modificationCounter++;
        return temp;
    }
    //Removes the last element in the list 'tail' and decrements the tail 
    //to whatever node was immediately previous to the tail. Must iterate 
    //through the entire list to find the new tail pointer.
    public E removeLast() {
        if (isEmpty()) 
            return null;        // Empty list, can't remove
        E temp = (E)tail.data;

        Node current = head, previous = null;
        while (current.next != null) {
            previous = current;
            current = current.next;
        }
        if (previous == null)       // one element in the list
            head = tail = null;
        else {
            previous.next = null;   // element at the tail
            tail = previous;
        }
        currentSize--;
        modificationCounter++;
        return temp;
    }
    //Searches for an object in the list by traversal if not found returns false
    public boolean find(E obj) {
        Node current = head;
        while (current != null) {
            if (obj ==(E)current.data)
                return true;
        current = current.next;
        }
        return false;
    }
    
    //finds smallest data element in the list. If list is empty returns null
    public E findMin() {
        if (isEmpty())
            return null;
        Node current = head;
        // Ensures that if the highest priority data is at the beginning of 
        // the list it is returned
        E highestPri = (E)current.data;

        if (currentSize == 1) {
            return (E)current.data;
        }
        while (current.next != null) {
            int comparison = 
                    ((Comparable<E>)highestPri).compareTo((E)current.data);
            if (comparison > 0) {
                highestPri = (E)current.data;
            }
            current = current.next;
        }
        return highestPri;
    }
    //finds largest data element in the list. If list is empty return null
    public E findMax() {
        if (isEmpty())
            return null;
        Node current = head;
        // Ensures that if the highest priority data is at the beginning of 
        // the list it is returned
        E highestPri = (E)current.data;

        if (currentSize == 1) {
            return (E)current.data;
        }
        while (current.next != null) {
            int comparison = 
                    ((Comparable<E>)highestPri).compareTo((E)current.data);
            if (comparison < 0) {
                highestPri = (E)current.data;
            }
            current = current.next;
        }
        return highestPri;
    }
    //Finds a specific object in the list and removes it, returning its value.
    //Returns null if list is empty or null if the object is not found.
    public E remove(E obj) {
        if (isEmpty()) 
            return null;
        
        Node current = head, previous = null;
        while (current != null && ((Comparable<E>)obj).
                compareTo((E)current.data) != 0) {
            previous = current;
            current = current.next;
        }
        if (current == null)                //obj not found
            return null;
        else if (previous == null)          //obj found at head, removeFirst()
            return this.removeFirst();
        else if (current == tail) {         //obj found at tail
            previous.next = null;           //instead of re-iterating thru the
            tail = previous;                //array just update tail here
        }
        else                                //obj found somewhere in the middle
            previous.next = current.next;
        
        currentSize--;
        modificationCounter++;
        return (E)current.data;
    }
    
    //Is the list empty?
    public boolean isEmpty() {
        return currentSize == 0;
    }
    //gets size
    public int size() {
        return currentSize;
    }
    //returns data at the head if head exists, if not returns null
    public E getHead() {
        if (head != null) 
            return (E)head.data;
        return null;
    }
    //  Returns the PQ to an empty state.
    public void clear() {
        head = null;
        currentSize = 0;
    }
    //Returns a fail-fast iterator in the correct order of the list. 
    //modCounter tracks to see if the iterator is corrupted by a removal and
    //throws ConcurrentModificationExcpetion.
    //remove() not supported, throws UnsupportedOperationException.
    public Iterator<E> iterator() {
        return new ListIteratorHelper();
    }
    class ListIteratorHelper implements Iterator<E> {

        Node<E> iterPtr;
        long modCounter;

        public ListIteratorHelper() {
            iterPtr = head;
            modCounter = modificationCounter;
        }

        public boolean hasNext() {
            if (modCounter != modificationCounter) {
                throw new ConcurrentModificationException();
            }
            return iterPtr != null;
        }

        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E temp = iterPtr.data;
            iterPtr = iterPtr.next;
            return temp;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
