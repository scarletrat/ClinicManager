package clinic;

/**
 * This class represents a Technician object, containing the profile of the technician, the location they work at,
 * and their rate per visit. Provider subclass.
 * @author Gordon Lin, Christopher Lee modified Oct. 16, 2024
 */
public class Technician extends Provider{
    private int ratePerVisit;

    /**
     * Constructor setting instance variable to parameters
     * @param profile profile object
     * @param location location object
     * @param ratePerVisit rate per visit variable
     */
    public Technician(Profile profile, Location location, int ratePerVisit) {
        super(profile, location);
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Sets the ratePerVisit instance variable to the parameter
     * @param ratePerVisit parameter to set the instance variable to.
     */
    public void setRatePerVisit(int ratePerVisit){
        this.ratePerVisit = ratePerVisit;
    }

    /**
     * Overrides rate method and gives the rate per visit instance variable
     * @return ratePerVisit variable
     */
    @Override
    public int rate() {
        return ratePerVisit;
    }

    /**
     * Overrides the equals method and compares the rate per visit and profile
     * @param obj object to be compared to
     * @return true if ratePerVisit and profile are equal
     */
    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj)) return false;
        if(obj instanceof Technician){
            Technician technician = (Technician) obj;
            return this.ratePerVisit == technician.ratePerVisit && this.getProfile() == technician.getProfile();
        }
        return false;
    }

    /**
     * Gives the output string to print the technician output
     * @return all the technician's information
     */
    @Override
    public String toString(){return super.toString() + "[rate: $" + ratePerVisit + ".00]";
    }

}
