package util;

import clinic.Technician;
/**
 * This class represents a node. The node has a pointer to the next node and Technician data
 * @author Gordon Lin, Christopher Lee modified Oct. 17, 2024
 */
public class Node {
    private Technician data;
    private Node next;

    /**
     * Constructor sets instance variables to parameters
     * @param technician technician object
     */
    public Node(Technician technician) {
        this.data = technician;
        this.next = null;
    }

    /**
     * Returns the node that this node is pointing to
     * @return next instance variable
     */
    public Node getNext(){
        return next;
    }

    /**
     * Returns data in this node
     * @return data instance variable
     */
    public Technician getData(){
        return data;
    }

    /**
     * Set the data of the node to the parameter.
     * @param technician the technician to be set to.
     */
    public void setData(Technician technician){
        this.data = technician;
    }

    /**
     * Sets node pointer to parameter
     * @param next parameter to set node next pointer to
     */
    public void setNext(Node next){
        this.next = next;
    }

}