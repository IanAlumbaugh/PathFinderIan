// == CS400 Fall 2024 File Header Information ==
// Name: Ian Alumbaugh
// Email: alumbaugh@wisc.edu
// Group: P2.3627
// Lecturer: Florian
// Notes to Grader: <optional extra notes>

import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

   protected LinkedList<Pair>[] table = null;

   @SuppressWarnings("unchecked")
   public HashtableMap(int capacity) {
	table = (LinkedList<Pair>[]) new LinkedList[capacity];
   }

   @SuppressWarnings("unchecked")
   public HashtableMap() { // with default capacity = 64
        table = (LinkedList<Pair>[]) new LinkedList[64];
   }

   protected class Pair {

	public KeyType key;
	public ValueType value;

	public Pair(KeyType key, ValueType value) {
		this.key = key;
		this.value = value;
	}

   }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     * @throws NullPointerException if key is null
     */
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
	//handler for if key is null, also init of hashCode
	if(key == null) { throw new NullPointerException(); }
	int hashCode = Math.abs(key.hashCode()) % getCapacity();

	//if key is a duplicate, throw an exception
	if(containsKey(key))
        	throw new IllegalArgumentException();

	//checks if array needs to be resized for a LF threshold of .8
	if((getSize() + 1) / (double)  getCapacity() >= 0.8)
		resizeHelper();

	//if hashCode index is empty, make a new list for it
	if(table[hashCode] == null)
		table[hashCode] = new LinkedList<Pair>();

	//add the new pair
	table[hashCode].add(new Pair(key, value));
    }

    /**
     * resizes the array to double what it is currently and rehashes
     */
    private void resizeHelper() {
	//inits new array with double old capacity
	LinkedList<Pair>[] resizedArray = (LinkedList<Pair>[]) new LinkedList[getCapacity() * 2];
	LinkedList<Pair>[] tempTable = table;
	int tempSize = getCapacity();
	table = resizedArray;

	//rehashes the new array
	for(int i = 0; i < tempSize; i++) {
		if(tempTable[i] != null) {
			for(int g = 0; g < tempTable[i].size(); g++) {
				put(tempTable[i].get(g).key, tempTable[i].get(g).value);
			}
		}
	}
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    public boolean containsKey(KeyType key) {
	//inits hashCode and then checks if given key at the index for
	//the hashCode contains key already, returns true if so
        int hashCode = Math.abs(key.hashCode()) % getCapacity();
	if(table[hashCode] != null) {
		for(int i = 0; i < table[hashCode].size(); i++) {
                	if(table[hashCode].get(i).key.equals(key)) {
                        	return true;
                	}
		}
	}

	//base case if key wasnt found
	return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType get(KeyType key) throws NoSuchElementException {
	//inits hashCode and checks if key is present first, if not throw exception
        int hashCode = Math.abs(key.hashCode()) % getCapacity();
	if(!containsKey(key))
		throw new NoSuchElementException();

	//inits the found value and searches through linkedList at hashCode index
	//if the key is found, return its value and stop searching
	ValueType foundValue = null;
	for(int i = 0; i < table[hashCode].size(); i++) {
		if(table[hashCode].get(i).key.equals(key)) {
			foundValue = table[hashCode].get(i).value;
			break;
		}
	}

        return foundValue;
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    public ValueType remove(KeyType key) throws NoSuchElementException {
        //inits hashCode and checks if key is present first, if not throw exception
	int hashCode = Math.abs(key.hashCode()) % getCapacity();
	if(!containsKey(key))
                throw new NoSuchElementException();

        //inits the removed value and searches through linkedList at hashCode index
        //if the key is found, return its value as removedValue and remove it after
        ValueType removedValue = null;
        for(int i = 0; i < table[hashCode].size(); i++) {
                if(table[hashCode].get(i).key.equals(key)) {
                        removedValue = table[hashCode].get(i).value;
			table[hashCode].remove(i);
                        break;
                }
        }

        return removedValue;
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    public void clear() {
	table = (LinkedList<Pair>[]) new LinkedList[getCapacity()];
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    public int getSize() {
	int size = 0;

	for(int i = 0; i < getCapacity(); i++) {
		if(table[i] != null) {
			for(int g = 0; g < table[i].size(); g++) {
				size++;
			}
		}
	}

        return size;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    public int getCapacity() {
	return table.length;
    }

    /**
     * Retrieves this collection's keys.
     * @return a list of keys in the underlying array for this collection
     */
    public List<KeyType> getKeys() {
	List<KeyType> keys = new LinkedList<KeyType>();

	//goes through table (array of pairs) and adds each key into keys list
	for(int i = 0; i < getSize(); i++) {
		if(table[i] != null) {
			for(int g = 0; g < table[i].size(); g++) {
                        	keys.add(table[i].get(g).key);
                        }
		}
	}

	return keys;
    }

   /**
    * tests put() method
    */
    @Test
    public void putTest() {
        //inits new Hashtable to be tested
        HashtableMap test = new HashtableMap(3);
	boolean pass = false;

	//tests put() method by adding 3 new pairs and checking if hashtable updates properly
	//in this case, proper rehashing and resizing is needed
        test.put("test1", "test1");
        test.put("test2", "test2");
        test.put("test3", "test3");
	if(!test.containsKey("test1") || !test.containsKey("test2") || !test.containsKey("test3") || test.getSize() != 3)
		Assertions.fail("put() method failed");

	//tests handling of duplicate keys
	try { test.put("test1", "test1"); }
	catch(IllegalArgumentException e) { pass = true; }
	if(!pass) { Assertions.fail("put() method failed to handle adding duplicate key"); }
	pass = false;

	//tests handling of null key 
	try { test.put(null, null); }
	catch(NullPointerException e) { pass = true; }
        if(!pass) { Assertions.fail("put() method failed to handle adding null key"); }
    }

   /**
    * tests containsKey() method
    */
    @Test
    public void containsTest() {
        //inits new Hashtable to be tested
        HashtableMap test = new HashtableMap(5);

        //tests containsKey() method by adding 3 new pairs and checking if they are added
        test.put("test1", "test1");
        test.put("test2", "test2");
        test.put("test3", "test3");
        if(!test.containsKey("test1") || !test.containsKey("test2") || !test.containsKey("test3"))
                Assertions.fail("containsKey() method failed");

	//test containsKey() method handling a key that is not present
	if(test.containsKey("FAIL")) { Assertions.fail("containsKey() method failed on finding nonexistent key"); }
    }

   /**
    * tests remove() methods
    */
    @Test
    public void removeTest() {
        //inits new Hashtable to be tested
        HashtableMap test = new HashtableMap(5);
	boolean pass = false;

        //tests remove() method by adding 3 new pairs and removing one
	//then checking to make sure hashtable updated properly
        test.put("test1", "test1");
        test.put("test2", "test2");
        test.put("test3", "test3");
	ValueType removed = (ValueType) test.remove("test2");
	if(test.containsKey("test2") || !removed.equals("test2")) { Assertions.fail("remove() method failed"); }

	//tests remove() method handling removing a key that isnt in hashtable
	try { test.remove("FAIL"); }
	catch(NoSuchElementException e) { pass = true; }
	if(!pass) { Assertions.fail("remove() method failed to handle removing a key not present"); }
    }

   /**
    * tests get() method
    */
    @Test
    public void getTest() {
        //inits new Hashtable to be tested
        HashtableMap test = new HashtableMap(5);
	boolean pass = false;

	//tests get() methods retrieving the proper key and not changing the hashtable
        test.put("test1", "test1");
        test.put("test2", "test2");
        test.put("test3", "test3");
	ValueType search = (ValueType) test.get("test2");
	if(!test.containsKey("test2") || !search.equals("test2")) { Assertions.fail("get() method failed"); }

        //tests get() method handling getting a key that isnt in hashtable
        try { test.get("FAIL"); }
        catch(NoSuchElementException e) { pass = true; }
        if(!pass) { Assertions.fail("get() method failed to handle getting a key not present"); }
    }

   /**
    * tests getSize() and getCapacity() and clear() methods
    */
    @Test
    public void sizeTest() {
	//inits new Hashtable to be tested
	HashtableMap test = new HashtableMap(5);

	//test case for getSize() method
	test.put("test1", "test1");
	test.put("test2", "test2");
	test.put("test3", "test3");
	if(test.getSize() != 3) { Assertions.fail("getSize() method failed"); }

	//test case for clear() method
	test.clear();
	if(test.getSize() != 0) { Assertions.fail("clear() method failed"); }

	//test case for getCapacity() method
	if(test.getCapacity() != 5) { Assertions.fail("getCapacity() method failed"); }
    }
}
