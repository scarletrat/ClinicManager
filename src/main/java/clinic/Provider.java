package clinic;

/**
 * This class includes the last name of the providers of the clinic
 * with the location and specialty of those providers.
 * It is an enum class and so far have 8 providers.
 * @author Gordon Lin, Christopher Lee modified Oct. 17, 2024
 */
public abstract class Provider extends Person {
    private Location location;
    public abstract int rate();

    /**
     * Constructor setting instance variables to parameters
     * @param fname first name variable
     * @param lname last name variable
     * @param dob date of birth variable
     * @param location location object
     */
    public Provider(String fname, String lname, String dob, Location location){
        super(fname,lname,dob);
        this.location = location;
    }

    /**
     * Constructor setting instance variables to parameters
     * @param profile profile object
     * @param location location object
     */
    public Provider(Profile profile,Location location){
        super(profile);
        this.location = location;
    }

    /**
     * Gives location object
     * @return location object
     */
    public Location getLocation(){
        return this.location;
    }

    /**
     * Return a textual representation of Provider.
     * @return return [name, location, specialty]
     */
    @Override
    public String toString(){
        return "[" + super.getProfile() + ", " + location + "]";

    }

}
