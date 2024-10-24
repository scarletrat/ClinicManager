package clinic;

/**
 * This class represents a Radiology object, containing enum constants.
 * @author Christopher Lee, Gordon Lin Oct. 17 2024
 */
public class Radiology {

    /**
     * Current service provided: catscan, ultrasound, and xray
     */
    public enum Service{
        CATSCAN, ULTRASOUND, XRAY
    }

    private Service service;

    /**
     * Constructor setting instance variable to enum constant
     * @param service parameter to decide what to set instance variable to
     */
    public Radiology(String service) {
        if (service.equalsIgnoreCase("xray")) {
            this.service = Service.XRAY;
        } else if (service.equalsIgnoreCase("ultrasound")) {
            this.service = Service.ULTRASOUND;
        } else if (service.equalsIgnoreCase("catscan")) {
            this.service = Service.CATSCAN;
        }
    }

    /**
     * Returns service enum constant
     * @return service
     */
    public Service getService(){return service;}

    /**
     * Overrides equals method to compare two services
     * @param obj Radiology object passed as parameter
     * @return true if both services are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Radiology) {
            Radiology radiology = (Radiology) obj; // Cast to Radiology
            // Compare the service types
            return this.service == radiology.service; // Compare enum values
        }
        return false;
    }

}
