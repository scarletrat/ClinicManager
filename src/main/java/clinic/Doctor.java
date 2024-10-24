package clinic;

/**
 * This class represents a Doctor object, containing the profile of the doctor, the location they work at,
 * their specialty, and their npi. Provider subclass.
 * @author Gordon Lin, Christopher Lee modified Oct. 16, 2024
 */
public class Doctor extends Provider{
    private Specialty specialty;
    private String npi;

    /**
     * Constructor setting instance variables to parameters. Uses super to call Provider constructor.
     * @param profile profile object
     * @param location location object
     * @param specialty specialty object
     * @param npi npi variable
     */
    public Doctor(Profile profile, Location location, Specialty specialty, String npi){
        super(profile,location);
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Returns the npi as a string
     * @return npi instance variable
     */
    public String getnpi(){
        return npi;
    }

    /**
     * Gets rate from enum class specialty
     * @return rate for doctors depending on specialty
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    /**
     * Overrides equals method to check if two doctor objects have the same npi
     * @param obj doctor object as parameter
     * @return true if both doctor's npi's are equal.
     */
    @Override
    public boolean equals(Object obj){
        if(!super.equals(obj)) return false;
        if(obj instanceof Doctor){
            Doctor doctor = (Doctor) obj;
            return this.npi.equals(doctor.npi);
        }
        return false;
    }

    /**
     * Overrides toString method to print doctor output
     * @return all the doctor's information
     */
    @Override
    public String toString(){
        return super.toString() + "[" + this.specialty + ", #" + this.npi +"]";
    }

}
