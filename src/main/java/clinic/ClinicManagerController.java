package clinic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import util.CircularLinkedList;
import util.Date;
import util.List;
import util.Sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Scanner;

public class ClinicManagerController {
    private List<Appointment> appointments = new List<>();
    private final List<Provider> providers = new List<>();
    private final List<Technician> technicians = new List<>();
    private CircularLinkedList rotation;

    @FXML
    TableView tbl_location;

    @FXML
    private TableColumn<Location, String> col_zip;
    @FXML
    private TableColumn<Location,String> col_county;
    @FXML
    private TableColumn<Location, Location> col_city;


    @FXML
    private ComboBox<String> cmb_provider;
    @FXML
    private ComboBox<String> cmb_timeslot;
    @FXML
    private TextArea outputArea;
    @FXML
    private Button loadButton;
    @FXML
    private RadioButton officeApt;
    @FXML
    private RadioButton imagingApt;
    @FXML
    private ToggleGroup apptType;
    @FXML
    private Button schedule;
    @FXML
    private DatePicker aptDate;
    @FXML
    private DatePicker birthDate;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;

    @FXML
    /**
     * This method is automatically performed after the fxml file is loaded.
     * Set the initial values for the GUI objects here.
     */
    public void initialize() {
        String[] time = new String[12];
        for (int i = 1; i < 13; i++) {
            Timeslot timeslot = new Timeslot(String.valueOf(i));
            time[i - 1] = timeslot.toString();
        }
        ObservableList<String> times =
                FXCollections.observableArrayList(time);
        cmb_timeslot.setItems(times);

        ObservableList<Location> locations =
                FXCollections.observableArrayList(Location.values());
        tbl_location.setItems(locations);

        col_zip.setCellValueFactory(new PropertyValueFactory<>("zip"));
        col_county.setCellValueFactory(new PropertyValueFactory<>("county"));
        col_city.setCellValueFactory(new PropertyValueFactory<>("name"));
    }


    @FXML
    /**
     *Load provider list to file.
     */
    void loadProviders(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Source File for the Import");
        chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File sourceFile = chooser.showOpenDialog(stage); //get the reference of the source file
        loadProvidersFromFile(sourceFile, providers, technicians);
        updateProvidersTable(providers);
    }

    private void loadProvidersFromFile(File file, List<Provider> providers, List<Technician> technicians) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String providerLine = scanner.nextLine();
                String[] providerData = providerLine.split("\\s+");
                Provider temp;

                Profile profile = new Profile(providerData[1], providerData[2], providerData[3]);
                Location location = Location.getLocation(providerData[4]);

                if (providerData[0].equals("D")) {  // Doctor
                    Specialty specialty = Specialty.getSpecialty(providerData[5]);
                    temp = new Doctor(profile, location, specialty, providerData[6]);
                } else {  // Technician
                    int rate = Integer.parseInt(providerData[5]);
                    temp = new Technician(profile, location, rate);
                    technicians.add((Technician) temp);  // Add to technician list
                }

                providers.add(temp);  // Add to general provider list
                System.out.println("Provider added: " + profile.getFname() + " " + profile.getLname());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    private void updateProvidersTable(List<Provider> providers) {
        int size = providers.size()- technicians.size();
        String[] names = new String[size];
        for (int i = 0; i < providers.size(); i++) {
            if(providers.get(i) instanceof Technician){
                break;
            }
            Profile profile = providers.get(i).getProfile();
            names[i] = profile.getFname() + " " + profile.getLname() + " (" + ((Doctor) providers.get(i)).getnpi() + ")";
        }

    ObservableList<String> providerNames =
            FXCollections.observableArrayList(names);
        System.out.println("Updating ComboBox with names: "+providerNames);  // Debug statement
        cmb_provider.setItems(providerNames);
        outputArea.appendText("Providers Loaded\n");
        loadButton.setDisable(true);
}

    @FXML
    /**
     * An event handler to disable Provider comboBox when Imaging is selected.
     */
    void imagingSelected(ActionEvent event) {
        cmb_provider.setDisable(imagingApt.isSelected());
    }

    @FXML
    /**
     * Schedule an appointment
     */
    void scheduleApt(ActionEvent event){
        if(!isValidAppointmentDate()){
            return;
        }

        if(fname.getText().isEmpty() || lname.getText().isEmpty()){
            outputArea.appendText("Please input your name.\n");
            return;
        }
        if(!isValidDob()){
            return;
        }
        if(!officeApt.isSelected()&&!imagingApt.isSelected()){
            outputArea.appendText("Please select an appointment type.\n");
            return;
        }
        if(officeApt.isSelected()){
            officeAppointment();
        }
        if(imagingApt.isSelected()){
            imagingAppointment();
        }

        //Appointment appointment = new Appointment(aptDate,timeslot,profile,doc);
        /*if(appointments.contains(appointment)){
            return appointment.getPatient().getProfile() + (" has an existing appointment at the same time slot.");
        }
        /*
        if(isDoctorFree(appointment.getDate(),appointment.getTimeslot(),doc)){
            appointments.add(appointment);
            return( appointment + " booked.");
        }
        return doc + (" is not available at slot " + inputPart[2]);
         */
    }

    /**
     * Scheduling An Office Type Appointment.
     */
    private void officeAppointment(){
        if(!checkTimeslot()){
            return;
        }
        if(cmb_provider.getValue()==null){
            outputArea.appendText("Please select a provider or load providers.\n");
            return;
        }
        Doctor doc = findDoc(cmb_provider.getValue());
        Appointment appointment = new Appointment(getAptDate(),getTimeslot(),getPatient(),doc);
        if(appointments.contains(appointment)){
            String out = appointment.getPatient().getProfile() + (" has an existing appointment at the same time slot.\n");
            outputArea.appendText(out);
            return;
        }
        if(isDoctorFree(getAptDate(),getTimeslot(),doc)){
            appointments.add(appointment);
            outputArea.appendText(appointment + " booked.\n");
            return;
        }
        int timeSlot = cmb_timeslot.getSelectionModel().getSelectedIndex() +1;
        outputArea.appendText( doc + " is not available at slot " + timeSlot +".\n");
    }

    /**
     * Scheduling An Imaging Type Appointment
     */
    private void imagingAppointment(){
        if(!checkTimeslot()){
            return;
        }
    }

    /**
     * Get Date object from LocalDate.
     * @param date the LocalDate from datePicker.
     * @return the date as a Date object.
     */
    private Date getDate(LocalDate date){
        String[] dates = date.toString().split("-");
        return new Date(dates[1],dates[2],dates[0]);
    }

    /**
     * Check if the doctor is free at that time.
     * @param date the appointment date.
     * @param timeslot the timeslot.
     * @param doc the doctor.
     * @return true if the doctor is available, return false otherwise.
     */
    private boolean isDoctorFree(Date date, Timeslot timeslot, Doctor doc){
        for(int i = 0; i<appointments.size();i++){
            Appointment appointment = appointments.get(i);
            if (appointment.getDate().equals(date) && appointment.getTimeslot().equals(timeslot) && appointment.getProvider().equals(doc)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the appointment timeslot.
     * @return the inputted timeslot as Timeslot object.
     */
    private Timeslot getTimeslot(){
        int index = cmb_timeslot.getSelectionModel().getSelectedIndex() + 1;
        String idi = String.valueOf(index);
        return new Timeslot(idi);
    }

    /**
     * Get the appointment date.
     * @return Date object of the appointment date.
     */
    private Date getAptDate(){
        LocalDate date = aptDate.getValue();
        return getDate(date);
    }

    /**
     * Get the patient.
     * @return Person object of patient.
     */
    private Person getPatient(){
        LocalDate date = birthDate.getValue();
        Date dob = getDate(date);
        return new Person(fname.getText(),lname.getText(), dob);
    }

    /**
     * Check if they selected a timeslot for the appointment.
     * @return true if they did, return false otherwise.
     */
    private boolean checkTimeslot(){
        int timeslotIndex = cmb_timeslot.getSelectionModel().getSelectedIndex();
        Timeslot timeslot = new Timeslot(String.valueOf(timeslotIndex));
        if(timeslot.getHour()==0&&timeslot.getMinute()==0){
            outputArea.appendText("Please select a timeslot.\n");
            return false;
        }
        return true;
    }

    /**
     * Check if the appointment date is valid appointment date.
     * @return true if it's valid, return false otherwise.
     */
    private boolean isValidAppointmentDate() {
        //dates[0] = year, 1 = month, 2 = date
        LocalDate date = aptDate.getValue();
        if (date == null) {
            outputArea.appendText("Please input an appointment date.\n");
            return false;
        }
        String[] dates = date.toString().split("-");
        Date aptDate = new Date(dates[1],dates[2],dates[0]);
        if (aptDate.isPast() || aptDate.isToday()) {
            outputArea.appendText ("Appointment date: " + aptDate + " is today or a date before today.\n");
            return false;
        }
        if (!aptDate.isWeekDay()) {
            outputArea.appendText ("Appointment date: " + aptDate + " is Saturday or Sunday.\n");
            return false;
        }
        if (!aptDate.within6MonthFromToday()) {
            outputArea.appendText ("Appointment date: " + aptDate + " is not within six months.\n");
            return false;
        }
        return true;
    }

    /**
     * Check if the inputted birthdate is a valid birthdate.
     * @return true if the birthDate is valid, return false otherwise.
     */
    private boolean isValidDob() {
        LocalDate date = birthDate.getValue();
        if (date == null) {
            outputArea.appendText("Please input your date of birth.\n");
            return false;
        }
        String[] dates = date.toString().split("-");
        Date birthDate = new Date(dates[1],dates[2],dates[0]);
        if (birthDate.isFuture() || birthDate.isToday()) {
            outputArea.appendText ("Patient dob: " + birthDate + " is today or a date after today.\n");
        }
        return true;
    }

    /**
     * Get the doctor from provider dropdown.
     * @param doc the doctor in string.
     * @return Doctor object of the dropdown.
     */
    private Doctor findDoc(String doc){
        String[] docPart = doc.split(" ");
        String fname = docPart[0];
        String lname = docPart[1];

        for(int i = 0; i< providers.size();i++){
            Profile temp = providers.get(i).getProfile();
            if(temp.getFname().equals(fname)&&temp.getLname().equals(lname)){
                return (Doctor)providers.get(i);
            }
        }
        return null;
    }

    @FXML
    /**
     *Clear all data on the screen.
     */
    void clear(ActionEvent event){
        aptDate.setValue(null);
        fname.clear();
        lname.clear();
        birthDate.setValue(null);
        apptType.selectToggle(null);
        cmb_provider.setDisable(imagingApt.isSelected());
        cmb_timeslot.getSelectionModel().clearSelection();
        cmb_provider.getSelectionModel().clearSelection();
    }
}
