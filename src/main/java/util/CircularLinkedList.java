package util;

/**
 * This class represents a circular linked list. uses nodes and pointers from the Node class
 * @author Gordon Lin, Christopher Lee modified Oct. 17, 2024
 */
public class CircularLinkedList {
    private Node last;  // Points to the last node in the list
    private int size;

    /**
     * Constructor sets last to null
     */
    public CircularLinkedList() {
        this.last = null;
        this.size = 0;
    }

    /**
     * Inserts node last spot of the list
     * @param e node to be inserted
     */
    public void insert(Node e) {

        if (last == null) {
            last = e;
            last.setNext(last);
        } else {
            e.setNext(last.getNext());
            last.setNext(e);
            last = e;
        }
        size++;
    }

    /**
     * Checks if the linked list has no nodes
     * @return true if last is null, false otherwise
     */
    public boolean isEmpty() {
        return last == null;
    }

    /**
     * Returns size of linked list
     * @return size instance variable
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns the last node in the linked list
     * @return last instance variable
     */
    public Node getLast() {
        return last;
    }

    /**
     * Sets the last node in the list to parameter
     * @param e parameter to be set to last node
     */
    public void setLast(Node e){
        this.last = e;
    }
}