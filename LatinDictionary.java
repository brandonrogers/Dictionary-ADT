/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author brandonrogers
 */
import java.util.Iterator;
import data_structures.*;
import java.lang.System;

public class LatinDictionary {
    private DictionaryADT<String,String> dictionary;
    private final int maxSize = 12000;

    // constructor takes no arguments.  Size depends on the datafile.
    // creates an instance of the DictionaryADT. Use your HashTable 
    // implementation in this class (though all four should work).
    // Methods that make modifications to the dictionary modify the
    // DictionaryADT object, not the datafile.
    public LatinDictionary() {
        dictionary = new HashTable<String,String>(maxSize);
    }

    // reads the key=value pairs from the datafile and builds a dictionary structure 
    public void loadDictionary(String fileName) {  
        DictionaryReader.getDictionaryArray(fileName);
        for (int i = 0; i < DictionaryReader.entries.length; i++ )
            dictionary.add(DictionaryReader.entries[i].getKey(), DictionaryReader.entries[i].getValue());
    
    }

    // inserts a new Latin word and its definition
    public boolean insertWord(String word, String definition) {
        return dictionary.add(word, definition);
    }

    // removes the key value pair that is identified by the key from the dictionary
    public boolean deleteWord(String word) {
        return dictionary.delete(word);
    }

    // looks up the definition of the Latin word
    public String getDefinition(String word) {
        return dictionary.getValue(word);
    }

    // returns true if the Latin word is already in the dictionary
    public boolean containsWord(String word) {
        return dictionary.contains(word);
    }
    
    // returns all of the keys in the dictionary within the range start .. finish
    // inclusive, in sorted order. Neither value 'start' or 'finish' need be in the
    // dictionary.  Returns null if there are no keys in the range specified.    
    public String[] getRange(String start, String finish) {
        UnorderedList<String> maxRange = new UnorderedList<String>();         
        String [] range; 
        String thisWord;
        int j = 0, currentSize = 0;
        
        Iterator<String> wordItr = words();
        while (wordItr.hasNext()) {
            thisWord = wordItr.next();
            if (thisWord.compareTo(finish) <= 0 && 
                    thisWord.compareTo(start) >= 0) {
                maxRange.insertLast(thisWord);
                currentSize++;
            }
        }
        range = new String[currentSize];
        for (String s : maxRange) {
            range[j] = s;
            j++;
        }
        return range;  
    }
            
    // returns an Iterator of the latin words (the keys) in the dictionary,
    // in sorted order.
    public Iterator<String> words() {
        return dictionary.keys();
    }

    // returns the definitions in the dictionary, in exactly the same order
    // as the words() Iterator
    public Iterator<String> definitions() {
        return dictionary.values();
    }
    
}   