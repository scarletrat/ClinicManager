package clinic;

/**
 * This class defines a node in a singly linked list that maintains the list of visits.
 * Each visit stores an appointment, and a pointer to the next appointment.
 * @author Gordon Lin, modified 9/28/2024
 */
public class Visit {
    private Appointment appointment;
    private Visit next;

    /**
     * Default no argument constructor.
     * Setting appointment and next pointer to null;
     */
    public Visit(){
        this.appointment = null;
        this.next = null;
    }

    /**
     * One argument constructor.
     * Setting appointment to appointment and next pointer to null.
     * @param appointment the first appointment of visit class.
     */
    public Visit(Appointment appointment){
        this.appointment = appointment;
        this.next = null;
    }

    /**
     * Get the appointment of this visit.
     * @return return the appointment.
     */
    public Appointment getAppointment(){
        return this.appointment;
    }

    /**
     * Get the next visit.
     * @return the next visit.
     */
    public Visit getNext(){
        return this.next;
    }

    /**
     * Set the current visit's appointment.
     * @param appointment the new appointment.
     */
    public void setAppointment(Appointment appointment){
        this.appointment = appointment;
    }

    /**
     * Set the next visit.
     * @param next the next visit.
     */
    public void setNext(Visit next){
        this.next = next;
    }

}
