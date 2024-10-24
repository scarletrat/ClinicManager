package util;


import java.util.Iterator;

/**
 * Manages array of appointments by sorting, adding elements, removing elements,
 * and adjusting the size of the array.
 * @author Christopher Lee, Gordon Lin modified Oct. 17, 2024.
 */
public class List<E> implements Iterable<E> {
    private E[] objects;
    private int size;
    private static final int NOT_FOUND = -1;

    /**
     *Constructor creates new array of objects
     */
    public List() {
        objects = (E[]) new Object[4];
        size=0;
    }

    /**
     * Finds generic e in list
     * @param e generic to find in list
     * @return returns index of generic or NOT_FOUND if not in list
     */
    private int find(E e){
        for(int i = 0; i<size; i++){
            if(objects[i].equals(e)){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Increases list size by 4
     */
    private void grow(){
        E[] newArray = (E[]) new Object[size+4];
        for(int i = 0; i<size; i++){
            newArray[i] = objects[i];
        }
        objects = newArray;
    }

    /**
     * Checks to see if list has generic e
     * @param e generic to search for in list
     * @return true if parameter is in list, false otherwise
     */
    public boolean contains(E e){
        return find(e) != NOT_FOUND;
    }

    /**
     * Adds generic to list
     * @param e generic to be added
     */
    public void add(E e){
        if(size == objects.length){
            grow();
        }
        objects[size] = e;
        size++;
    }

    /**
     * Removes generic from list
     * @param e generic to be removed if it exists
     */
    public void remove(E e){
        int index = indexOf(e);
        if(index != NOT_FOUND){
            for(int i= index; i<size-1 ; i++){
                objects[i] = objects[i+1];
            }
            //set last element to null
            objects[size-1] = null;
            size = size -1;
        }
    }

    /**
     * Checks to see if list is empty
     * @return true if size is 0, false otherwise.
     */
    public boolean isEmpty(){
        return size ==0;
    }

    /**
     * Returns size of list
     * @return size variable
     */
    public int size(){
        return size;
    }

    /**
     * Gives new iterator object
     * @return iterator object
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator<E>();
    }

    /**
     * Returns the generic at the index
     * @param index parameter used for the index of the object
     * @return gives the generic at the index
     */
    public E get(int index){
        if(index <size) {
            return objects[index];
        }
        return null;
    }

    /**
     * Sets the generic to the index in the list
     * @param index parameter of index to set generic to
     * @param e generic to be placed at the index
     */
    public void set(int index, E e){
        if (index >= 0 && index < size) {
            objects[index] = e; // Set the element at the specified index
        } else {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Returns the index of an item in the list
     * @param e the item to find in the list
     * @return index of the item
     */
    public int indexOf(E e){
        return find(e);
    }

    /**
     * Implements Iterator to access elements of the list sequentially.
     * @param <E> E generic object.
     */
    private class ListIterator<E> implements Iterator<E>{
        private int currentIndex = 0;

        /**
         * Checks to see if there is another element next
         * @return true if there are still more elements in the list, false otherwise
         */
        public boolean hasNext(){
            return currentIndex < size;
        }

        /**
         * Returns next element in the list
         * @return next element
         */
        public  E next(){
            if (!hasNext()){
                throw new java.util.NoSuchElementException("No more elements");
            }
            return (E) objects[currentIndex++];
        }
    }
}