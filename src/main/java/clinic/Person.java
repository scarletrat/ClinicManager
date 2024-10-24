package clinic;
/**
 * This class represents a Person object and contains a profile.
 * @author Gordon Lin, Christopher Lee modified Oct. 17, 2024
 */
public class Person {
    protected Profile profile;

    /**
     * Default constructor creating a new profile
     */
    public Person(){
        profile = new Profile();
    }

    /**
     * Constructor setting instance variable to parameter
     * @param profile profile object
     */
    public Person(Profile profile){
        this.profile = profile;
    }

    /**
     * Constructor setting instance variable to parameters
     * @param fname first name variable
     * @param lname last name variable
     * @param dob date of birth variable
     */
    public Person(String fname, String lname, String dob){
        this.profile = new Profile(fname,lname,dob);
    }

    /**
     * Gets profile instance variable
     * @return profile object
     */
    public Profile getProfile(){
        return profile;
    }

    /**
     * Sets instance variable profile to parameter
     * @param profile profile object passed as parameter
     */
    public void setProfile(Profile profile){
        this.profile = profile;
    }

    /**
     * Overrides compareTo method to compare two Person object's profiles
     * @param person Person object to be compared to
     * @return 1 if this Person's profile is greater than input Person's profile.
     * return 0 if this Person's profile is equal to input Person's profile
     * return -1 if this Person's profile is less than input Person's profile
     */
    public int compareTo(Person person) {
        return profile.compareTo(person.getProfile());
    }

    /**
     * Overrides equals method to compare
     * @param obj object to be compared to
     * @return true if both Person object's profiles are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Person){
            Person person = (Person) obj;
            return this.profile.equals(person.profile);
        }
        return false;
    }

    /**
     * Returns the profile information
     * @return profile first name, last name, and date of birth
     */
    @Override
    public String toString(){
        return this.profile.toString();
    }

}
