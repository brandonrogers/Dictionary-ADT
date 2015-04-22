/*
 * @author brandonrogers
 * BrandonsDictionaryTester.java
 * Tests all methods in the DictionaryADT
 * Thanks to Professor Riggins for the randomString method
 */
import data_structures.*;
import java.util.Iterator;

public class BrandonsDictionaryTester {
    public static void main(String [] args) {
        new BrandonsDictionaryTester();
    }
    
    final int DEFAULT_MAX = 10;             // small array to test methods
    DictionaryADT dictionary;
    int maxSize;
    
    public BrandonsDictionaryTester() {
//          dictionary = new OrderedArrayDictionary(DEFAULT_MAX);  // change for larger arrays
//          dictionary = new HashTable(12000);                   // 12000 equals my max_Size                
//            dictionary = new BinarySearchTree();
            dictionary = new RedBlackTree();
// Change the line above to test different implementations.  
            
        System.out.println("Testing the " + dictionary.getClass() + " implementation");
        
        String[] smallArray = getRandomStrings(DEFAULT_MAX);
        Integer[] intArray = new Integer[10];
        // String[] hugeArray = getRandomStrings(DEFAULT_MAX*100000);
        for (int i = 0; i < 10; i++) {
            intArray[i] = i+1;
        }
        System.out.println("Testing the add method: ");
        if (
            dictionary.add(intArray[5], smallArray[0]) &&
            dictionary.add(intArray[7], smallArray[1]) &&
            dictionary.add(intArray[1], smallArray[2]) &&
            dictionary.add(intArray[4], smallArray[3]) &&
            dictionary.add(intArray[9], smallArray[4]) &&
            dictionary.add(intArray[0], smallArray[5]) &&
            dictionary.add(intArray[3], smallArray[6]) &&
            dictionary.add(intArray[2], smallArray[7]) &&
            dictionary.add(intArray[8], smallArray[8]) &&
            dictionary.add(intArray[6], smallArray[9])
                    )
            System.out.println("Add successful");
        else
            System.out.println("Add failed!");
        
        System.out.println("Testing the key iterator method: ");
            Iterator keyItr = dictionary.keys();
            while (keyItr.hasNext()) {
                System.out.println("Keys in order: " + keyItr.next());
            }
        
        System.out.println("Testing the value iterator method, should be in key order: ");
            Iterator valItr = dictionary.values();
            int i = 0;
            while (valItr.hasNext()) {
                System.out.println("Value at key: " + (i+1) + " = " + 
                        valItr.next());
                i++;
            }
        
        System.out.println("Testing the getValue method, should return same order as the value iterator: ");
            for (Integer j : intArray) {
                System.out.println("Value at key " + j + " is " + 
                        dictionary.getValue(j));
            }
        
        System.out.println("Testing the getKey method:");
        System.out.println("During the add method the string value at index 3 was: " + smallArray[7]);
        System.out.println("getKey returns: " + dictionary.getKey(smallArray[7]));
        
        
        System.out.println("Testing the size method: " + dictionary.size());
        System.out.println("Testing the isEmpty method: " + dictionary.isEmpty());
        System.out.println("Testing the isFull method: (call should be "
                + "true for arrays, false for everything else) : " + dictionary.isFull());
        System.out.println("Testing the delete method: key = '5' (deletes the root in BinarySearchTree implementations)");
        
        if(dictionary.delete(5))
            System.out.println("Delete successful");
        else
            System.out.println("Delete failed! in BST: failed to delete root");
        System.out.println("Attempting to search for the deleted key with contains: ");
        
        if (dictionary.contains(5))
            System.out.println("Error: found deleted key. check your delete method");
        else
            System.out.println("Not found, good");
        
        System.out.println("Attempting to search for the deleted key with getValue: ");
        if (dictionary.getValue(5) != null) 
            System.out.println("Error: returned deleted key-value pair. check your delete method");
        else
            System.out.println("Not found, good");
        
        System.out.println("Attempting to search for a deleted value: ");
        if (dictionary.getKey(smallArray[3]) != null)
            System.out.println("Error, getKey returned deleted key from value" + smallArray[0]);
        else
            System.out.println("Not found, good");
        
        System.out.println("Testing the clear method.................");
        dictionary.clear();
        
        System.out.println("Testing the isEmpty method: " + dictionary.isEmpty());
        
        System.out.println("Attempting to search for a deleted key: ");
        if (dictionary.contains(4))
            System.out.println("Error: returned deleted key. check your clear method");
        else
            System.out.println("Not found, good");
        
        System.out.println("Attempting to search for a deleted value: ");
        if (dictionary.getValue(smallArray[7]) != null) {
            System.out.println("Error: returned deleted value. check your clear method");
        }
        else
            System.out.println("Not found, good");
    }   
    
    private static String[] getRandomStrings(int size) {
        
                String [] str = new String[size];
                String temp = "";
                int where=0;
                byte [] b = {0x41,0x41,0x41,0x30,0x30,0x30};
        
        for(int i=0; i < 10; i++)
            for(int j=0; j < 10; j+=(int)2*Math.random()+1)
                for(int k=0; k < 10; k+=(int)2*Math.random()+1)
                    for(int l=0; l < 26; l+=(int)2*Math.random()+1)
                        for(int m=0; m < 26; m+=(int) 2*Math.random()+1)
                            for(int n=0; n < 26; n++) {
                                if(where >= size) break; 
                                str[where++] = ""+                             
                                ((char)(b[0]+n)) +                                
                                ((char)(b[1]+m)) +
                                ((char)(b[2]+l)) +
                                ((char)(b[3]+k)) +
                                ((char)(b[4]+j)) +
                                ((char)(b[5]+i));
                            }
        for(int i=0; i < size; i++) {
            int index = ( (int) (size*Math.random()));
            String tmp = str[index];
            str[index] = str[i];
            str[i] = tmp;
            }                            
                            
        return str;
        }
}
