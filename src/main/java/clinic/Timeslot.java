package clinic;

/**
 * This class defines the time slots of a day in the format HH:MM.
 * It is an enum class and have only 6 slots specified by the clinic.
 * @author Gordon Lin, modified Oct. 17, 2024.
 */
public class Timeslot implements Comparable<Timeslot> {
    public static final int NOON = 12;

    private int hour;
    private int minute;

    /**
     * Constructor setting instance variables based on parameter
     * @param timeslot timeslot variable to set instance variables to
     */
    public Timeslot(String timeslot){
        hour = 0;
        minute = 0;
        switch (timeslot) {
            case "1" -> hour = 9;
            case "2" -> {
                hour = 9;
                minute = 30;
            }
            case "3" -> hour = 10;
            case "4" -> {
                hour = 10;
                minute = 30;
            }
            case "5" -> hour = 11;
            case "6" -> {
                hour = 11;
                minute = 30;
            }
            case "7" -> hour = 14;
            case "8" -> {
                hour = 14;
                minute = 30;
            }
            case "9" -> hour = 15;
            case "10" -> {
                hour = 15;
                minute = 30;
            }
            case "11" -> hour = 16;
            case "12" -> {
                hour = 16;
                minute = 30;
            }
        }
    }

    /**
     * Get the hour of the timeslot.
     * @return return the hour of the timeslot.
     */
    public int getHour(){
        return hour;
    }

    /**
     * Get the minute of the timeslot.
     * @return return the minute of the timeslot.
     */
    public int getMinute(){
        return minute;
    }

    /**
     * Comparing two timeslot object.
     * @param o the object to be compared.
     * @return 1 if the timeslot is later than Timeslot o,
     * return -1 if the timeslot is earlier than Timeslot o,
     * return 0 if the same.
     */
    @Override
    public int compareTo(Timeslot o) {
        int hourComparison = Integer.compare(this.getHour(), o.getHour());
        if (hourComparison != 0) {
            return hourComparison;
        }
        return Integer.compare(this.getMinute(), o.getMinute());
    }

    /**
     * Overrides equals method to compare the minute and hour instance variables
     * @param obj object to be compared to
     * @return true if this minute and hour are equal to obj minute and hour, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Timeslot){
            Timeslot timeslot = (Timeslot) obj;
            return this.minute == timeslot.minute
                    &&this.hour == timeslot.hour;
        }
        return false;
    }

    /**
     * Return a textual representation of the timeslot.
     * @return return a string containing the hour:minute.
     */
    @Override
    public String toString(){
        String time;
        int hour;
        if(this.hour>NOON){
            hour = this.hour -12;
            time = "PM";
        }else{
            hour = this.hour;
            time = "AM";
        }
        if(this.minute == 0){
            return(hour + ":00" + " " + time);
        }
        return(hour + ":" + this.minute + " " + time);
    }

}
