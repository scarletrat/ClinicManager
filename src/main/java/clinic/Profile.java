package clinic;
import util.Date;

/**
 * This class models a patient's profile.
 * It will include their first and last name and date of birth.
 * It implements Java Comparable Interface.
 * @author Gordon Lin modified Oct. 17, 2024.
 */
public class Profile implements Comparable<Profile>{
    protected String fname;
    protected String lname;
    protected Date dob;

    /**
     * A default/no-argument constructor
     * Set the profile first and last name to "default" and dob to today.
     */
    public Profile(){
        this.fname = "default";
        this.lname = "default";
        this.dob = new Date();
    }

    /**
     * Constructor with 3 parameters.
     * @param fname the first name of the profile.
     * @param lname the last name of the profile.
     * @param dob the date of birth of the profile.
     */
    public Profile(String fname, String lname, Date dob){
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Constructor with 3 parameters.
     * @param fname the first name of the profile.
     * @param lname the last name of the profile.
     * @param dob the date of birth of the profile in String.
     */
    public Profile(String fname, String lname, String dob){
        this.fname = fname;
        this.lname = lname;
        this.dob = new Date(dob);
    }

    /**
     * Get the first name of the profile.
     * @return return the first name of the profile.
     */
    public String getFname(){ return fname;}

    /**
     * Get the last name of the profile.
     * @return return the last name of the profile.
     */
    public String getLname(){ return lname;}

    /**
     * Get the date of birth of the profile in String.
     * @return return the date of birth of the profile in string.
     */
    public String getDob_inString(){ return dob.toString();}

    /**
     * Get the date of birth of the profile in Date object.
     * @return return the date of birth of the profile in Date object.
     */
    public Date getDob_inDate(){ return dob;}

    /**
     * Set the profile's first name to the given string.
     * @param fname the new first name of the profile.
     */
    public void setFname(String fname){ this.fname = fname;}

    /**
     * Set the profile's last name to the given string.
     * @param lname the new last name of the profile.
     */
    public void setLname(String lname){ this.lname = lname;}

    /**
     * Set the profile's date of birth to the given date Object.
     * @param dob the new date of birth of the profile.
     */
    public void setDob_inDate(Date dob){ this.dob = dob;}

    /**
     * Set the profile's date of birth to the given String.
     * @param dob the new date of birth of the profile in String.
     */
    public void setDob_inString(String dob){
        this.dob = new Date(dob);
    }

    /**
     * Compare two Profile objects.
     * @param profile the object to be compared.
     * @return return 1 if either lname, fname, dob in this order of this profile is after input "profile" dob;
     * return -1 if either lname, fname, dob in this order of this profile is before input "profile" dob;
     * return 0 if equal.
     */
    @Override
    public int compareTo(Profile profile){
        if(this.lname.toLowerCase().compareTo(profile.lname.toLowerCase()) != 0){
            if(this.lname.toLowerCase().compareTo(profile.lname.toLowerCase())<0)
                return -1;
            else{
                return 1;
            }
        }
        if(this.fname.toLowerCase().compareTo(profile.fname.toLowerCase()) != 0){
            if(this.fname.toLowerCase().compareTo(profile.fname.toLowerCase())<0)
                return -1;
            else{
                return 1;
            }
        }
        if(this.dob.compareTo(profile.dob) != 0){
            return this.dob.compareTo(profile.dob);
        }
        return 0;
    }

    /**
     * Compare to see if two profile are equal.
     * @param obj the object to be compared to.
     * @return return true if two profile objects are equal; return false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof Profile){
            Profile profile = (Profile) obj;
            return this.fname.equalsIgnoreCase(profile.fname)
                    && this.lname.equalsIgnoreCase(profile.lname)
                    &&this.dob.equals(profile.dob);
        }
        return false;
    }

    /**
     * A textual representation of the Profile object.
     * @return return a string containing first nam, last name, and date of birth.
     */
    @Override
    public String toString(){
        return(fname + " " + lname + " " + dob);
    }

}
