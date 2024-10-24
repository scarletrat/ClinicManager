package clinic;
import util.Date;

/**
 * This class represents an Imaging object, containing the appointment instance variables and a radiology object.
 * @author Christopher Lee, Gordon Lin, modified Oct. 17, 2024
 */
public class Imaging extends Appointment{
    private Radiology room;

    /**
     * Constructor that sets instance variables to parameters
     * @param date date object
     * @param timeslot timeslot object
     * @param profile profile object
     * @param room room variable
     * @param technician technician object
     */
    public Imaging(Date date, Timeslot timeslot, Person profile, String room, Technician technician){
        super(date, timeslot, profile, technician);
        this.room = new Radiology(room);
    }

    /**
     * Gives the room of the Imaging object
     * @return room object
     */
    public Radiology getRadiology(){
        return room;
    }

    /**
     * Overrides equals method to compare the rooms of Imaging objects
     * @param obj object to be compared to
     * @return 1 if this room is greater than input room
     * return 0 if this room is equal to input room
     * return -1 if this room is less than input room
     */
    @Override
    public boolean equals(Object obj){
        if (!super.equals(obj)) return false;
        if( obj instanceof  Imaging){
            Imaging imaging = (Imaging) obj;
            return this.room.equals(imaging.room);
        }
        return false;
    }

    /**
     * The method returns a String representation of the Imaging object.
     * @return return a string representation of the object.
     */
    @Override
    public String toString(){
        return this.date + " " + this.timeslot + " " +
                this.patient + " " + this.provider +"[" + this.getRadiology().getService() + "]";
    }

}
