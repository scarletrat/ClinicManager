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
import util.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Scanner;

public class ClinicManagerController {
    private List<Appointment> appointments = new List<>();
    private final List<Provider> providers = new List<>();
    private final List<Technician> technicians = new List<>();
    private final CircularLinkedList rotation = new CircularLinkedList();
    private final List<Patient> patients = new List<>();


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
    private ComboBox<String> cmb_options;
    @FXML
    private ComboBox<String> cmb_timeslot;
    @FXML
    private ComboBox<String> cmb_timeslotR;
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
    ObservableList<String> providerNames;
    ObservableList<String> services;


    @FXML
    /**
     * This method is automatically performed after the fxml file is loaded.
     * Set the initial values for the GUI objects here.
     */
    void initialize() {
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
        outputArea.appendText("Clinic Manager is running...\n");

        String[] options = {"Appointments by date","Appointments by patient","Appointments by location","Office Appointments","Imaging Appointments","Providers' credit amount","Patients' bill"};
        ObservableList<String> optionsList = FXCollections.observableArrayList(options);
        cmb_options.setItems(optionsList);
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
        if(sourceFile == null){
            return;
        }
        loadProvidersFromFile(sourceFile, providers, technicians);
        updateProvidersTable(providers);
        displayProviderList();
        cmb_provider.getSelectionModel().clearSelection();
        outputArea.appendText("Rotation list for the technicians.\n");
        createRotation();
    }

    /**
     *Load providers from input txt file.
     * @param file the file.
     * @param providers providers list.
     * @param technicians technicians list.
     */
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
            }
        } catch (FileNotFoundException e) {
            outputArea.appendText("File not found: " + e.getMessage());
        }
    }

    /**
     * Updates the providers comboBox
     * @param providers the providers list
     */
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

        services = FXCollections.observableArrayList();
        for (Radiology.Service service : Radiology.Service.values()) {
            services.add(service.toString()); // Or specialty.toString()
        }
        providerNames = FXCollections.observableArrayList(names);
        cmb_provider.setItems(providerNames);
        if(imagingApt.isSelected()){
            cmb_provider.setItems(services);
        }
        outputArea.appendText("Providers Loaded\n");
        loadButton.setDisable(true);
    }

    /**
     * This method display the provider's list.
     */
    private void displayProviderList(){
        Sort.provider(providers);
        for (int i = 0; i<providers.size(); i++){
            outputArea.appendText(providers.get(i).toString() +"\n");
        }
    }

    /**
     * This method creates a circular linked list in order by location and prints out the rotation list.
     */
    private void createRotation(){
        int size = technicians.size();
        for(int i = size-1; i >= 0; i--){
            Node node = new Node(technicians.get(i));
            rotation.insert(node);
            if(i == 0){
                outputArea.appendText(technicians.get(0).getProfile().getFname() + " " +
                        technicians.get(0).getProfile().getLname() + " (" +
                        technicians.get(0).getLocation().getName() + ")");
                break;
            }
            outputArea.appendText(technicians.get(i).getProfile().getFname() + " " +
                    technicians.get(i).getProfile().getLname() + " (" +
                    technicians.get(i).getLocation().getName() + ") --> ");
        }
        outputArea.appendText("\n");
    }

    @FXML
    /**
     * An event handler to disable Provider comboBox when Imaging is selected.
     */
    void imagingSelected(ActionEvent event) {
        // Set the items and prompt text based on the selected radio button
        if (imagingApt.isSelected()) {
            cmb_provider.setItems(services);
            cmb_provider.setPromptText("Service");
        } else {
            cmb_provider.setItems(providerNames);
            cmb_provider.setPromptText("Providers");
        }
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
        if(cmb_provider.getValue()==null){
            outputArea.appendText("Please select an Imaging Service.\n");
            return;
        }
        if(providers.isEmpty()){
            outputArea.appendText("Please load providers.\n");
            return;
        }
        Person patient = getPatient();
        if(!isValidAppointment(patient.getProfile(),getAptDate(),getTimeslot())) {
            outputArea.appendText(patient.getProfile() + (" has an existing appointment at the same time slot.\n"));
            return;
        }
        Radiology radiology = new Radiology(cmb_provider.getValue());
        int currentServices = isServiceAvailable(getTimeslot(), getAptDate(), radiology);
        if(currentServices>=2) {
            outputArea.appendText("Cannot find an available technician at all locations for " + radiology.getService()
                    + " at " + cmb_timeslot.getValue() + ".\n");
            return;
        }
        Technician technician = null;
        if(currentServices == 1) {
            technician = findTechnician(findLocation(getAptDate(), getTimeslot(), radiology), getAptDate(), getTimeslot());
        } else if (currentServices ==0) {
            technician = findTechnician(getAptDate(), getTimeslot());
        }
        Appointment imaging = new Imaging(getAptDate(), getTimeslot(), patient, cmb_provider.getValue(), technician);
        appointments.add(imaging);
        outputArea.appendText(imaging + " booked.\n");
    }

    /**
     * This method check if the imaging service is available.
     * @param timeslot the timeslot of the appointment.
     * @param date the date of the appointment.
     * @param service the imaging service.
     * @return return 0 if the service is available in all locations,
     * return 1 if the service is available in only 1 location.
     * return 2 if all locations are booked.
     */
    private int isServiceAvailable(Timeslot timeslot, Date date, Radiology service){
        int count = 0;
        for (int i = 0; i < appointments.size(); i++){
            if(appointments.get(i) instanceof Imaging){
                if(appointments.get(i).getDate().equals(date)
                        &&appointments.get(i).getTimeslot().equals(timeslot)
                        &&service.getService().equals(((Imaging)appointments.get(i)).getRadiology().getService())){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * This method check if the technician is available at the time and date.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @param technician the technician that we're checking.
     * @return return true if the technician is available,
     * return false otherwise.
     */
    private boolean isTechnicianFree(Date date, Timeslot timeslot, Technician technician)  {
        for(int i = 0; i < appointments.size(); i++){
            Sort.appointment(appointments,'I');
            Appointment appointment = appointments.get(i);
            if(appointment instanceof Imaging){
                if(appointment.getDate( ).equals(date) && appointment.getTimeslot().equals(timeslot) && appointment.getProvider().getProfile().equals(technician.getProfile()))
                    return false;
            }else{
                break;
            }
        }
        return true;
    }

    /**
     * This method find the available technician in the date and timeslot and one that's not on the location listed.
     * @param location the location that's not available.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return return technician that's available at the specified constraints.
     */
    private Technician findTechnician(Location location, Date date, Timeslot timeslot){
        for(int i = 0; i < rotation.getSize(); i++){
            rotation.setLast(rotation.getLast().getNext());
            Technician technician = rotation.getLast().getData();
            if(!technician.getLocation().equals(location) && isTechnicianFree(date,timeslot,technician)){
                return technician;
            }
        }
        return null;
    }

    /**
     * This method find the available technician in the date and timeslot,
     * given that all locations are available.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return return technician.
     */
    private Technician findTechnician(Date date, Timeslot timeslot) {
        for (int i = 0; i < rotation.getSize(); i++) {
            // Move to the next technician
            rotation.setLast(rotation.getLast().getNext());
            Technician technician = rotation.getLast().getData();
            // Check if the current technician is free
            if (isTechnicianFree(date, timeslot, technician)) {
                return technician;
            }
        }
        // Return null if no technician is found
        return null;
    }

    /**
     * This method find the location give the date, timeslot, and imaging service appointment.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @param service the service.
     * @return return the location of the imaging appointment.
     */
    private Location findLocation(Date date, Timeslot timeslot, Radiology service) {
        for (int i = 0; i < appointments.size(); i++) {
            Sort.appointment(appointments,'I');
            Appointment appointment = appointments.get(i);
            if(appointment instanceof Imaging){
                if(appointment.getDate().equals(date) && appointment.getTimeslot().equals(timeslot) && ((Imaging) appointment).getRadiology().equals(service)){
                    Technician technician = (Technician) appointment.getProvider();
                    return technician.getLocation();
                }
            }else{
                break;
            }
        }
        return null;
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
     * Get the rescheduled timeslot.
     * @return the inputted timeslot as Timeslot object.
     */
    private Timeslot getTimeslotR(){
        int index = cmb_timeslotR.getSelectionModel().getSelectedIndex() + 1;
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
     * Check if they selected a timeslot for the appointment.
     * @return true if they did, return false otherwise.
     */
    private boolean checkTimeslotR(){
        int timeslotIndex = cmb_timeslotR.getSelectionModel().getSelectedIndex();
        Timeslot timeslot = new Timeslot(String.valueOf(timeslotIndex));
        if(timeslot.getHour()==0&&timeslot.getMinute()==0){
            outputArea.appendText("Please select a timeslot to reschedule.\n");
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
            return false;
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
     * Cancel an existing appointment
     */
    void cancelApt(ActionEvent event){
        if(!isValidAppointmentDate()){
            return;
        }
        if(fname.getText().isEmpty() || lname.getText().isEmpty()){
            outputArea.appendText("Please input your name.\n");
            return;
        }
        if(!isValidDob() ||!checkTimeslot()){
            return;
        }
        Date date = getAptDate();
        Timeslot timeslot = getTimeslot();
        Profile profile = getPatient().getProfile();
        if (!isValidAppointment(profile, date, timeslot)) {
            removeAppointment(date, timeslot, profile);
            outputArea.appendText( date+ " " + timeslot + " " + profile + " - appointment has been canceled.\n");
            return;
        }
        outputArea.appendText(date + " " + timeslot + " " + profile + " - appointment does not exist.\n");
    }

    /**
     * Remove the appointment from the list.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment,
     * @param profile the patient's profile.
     */
    private void removeAppointment(Date date, Timeslot timeslot, Profile profile){
        for(int i =0; i< appointments.size(); i++){
            Appointment appointment = appointments.get(i);
            if(appointment.getDate().equals(date) && appointment.getTimeslot().equals(timeslot) && appointment.getPatient().getProfile().equals((profile))){
                appointments.remove(appointment);
            }
        }
    }

    /**
     * Check if appointment with the same patient profile, date, and timeslot already exists.
     * @param profile the patient's profile.
     * @param date the appointment date.
     * @param timeslot the appointment timeslot.
     * @return return true if it doesn't exist
     * return false if the appointment already exist.
     */
    private boolean isValidAppointment(Profile profile, Date date, Timeslot timeslot) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getPatient().getProfile().equals(profile) && appointment.getDate().equals(date) && appointment.getTimeslot().equals(timeslot)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    /**
     * Reschedule an existing office type appointment with the same provider.
     */
    void rescheduleApt(ActionEvent event){
        if(!isValidAppointmentDate()){
            return;
        }
        if(fname.getText().isEmpty() || lname.getText().isEmpty()){
            outputArea.appendText("Please input your name.\n");
            return;
        }
        if(!isValidDob() || !checkTimeslot() || !checkTimeslotR()){
            return;
        }
        Date date = getAptDate();
        Timeslot timeslot = getTimeslot();
        Profile profile = getPatient().getProfile();
        if (isValidAppointment(profile, date, timeslot)) {
            outputArea.appendText( date+ " " + timeslot + " " + profile + " does not exist.\n");
            return;
        }
        outputArea.appendText(date + " " + timeslot + " " + profile + " - appointment does not exist.\n");
        Timeslot newTimeslot = getTimeslotR();
        //do they have the appointment at the newTimeslot?
        if(hasAppointmentAtTimeslot(date,newTimeslot,profile)){
            outputArea.appendText( profile + " has an existing appointment at " + date + " " + newTimeslot +"\n");
            return;
        }
        //is the provider free at that timeslot?
        Doctor doc = findDoctor(profile,date,timeslot);
        if(isDoctorFree(date, newTimeslot, doc)){
            Appointment old = new Appointment(date,timeslot,getPatient(),doc);
            appointments.remove(old);
            Appointment newApp = new Appointment(date, newTimeslot, getPatient(),doc);
            appointments.add(newApp);
            outputArea.appendText("Rescheduled to " + date + " " + newTimeslot+ " " + profile + " " + doc + "\n");
            return;
        }
        outputArea.appendText( doc + " is not available at slot " + newTimeslot+"\n");
    }

    /**
     * Find if the patient has the appointment at the time specified.
     * @param date the appointment date.
     * @param timeslot the appointment timeslot.
     * @param profile the patient's profile.
     * @return return true if the patient has the appointment at that time,
     * return false otherwise.
     */
    private boolean hasAppointmentAtTimeslot(Date date, Timeslot timeslot,Profile profile){
        for(int i =0; i< appointments.size(); i++){
            Appointment appointment = appointments.get(i);
            if(appointment.getTimeslot().equals(timeslot) && appointment.getPatient().getProfile().equals(profile) && appointment.getDate().equals(date)){
                return true;
            }
        }
        return false;
    }

    /**
     * Find the Doctor given the patient profile, appointment date and timeslot.
     * @param profile the patient's profile.
     * @param date the date of the appointment.
     * @param timeslot the timeslot of the appointment.
     * @return return the doctor at the appointment with the same timeslot, date, and patient.
     */
    private Doctor findDoctor(Profile profile, Date date, Timeslot timeslot) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getPatient().getProfile().equals(profile) && appointment.getDate().equals(date) && appointment.getTimeslot().equals(timeslot)) {
                if(appointment.getProvider() instanceof Doctor) return (Doctor) appointment.getProvider();
            }
        }
        return null;
    }

    @FXML
    void printButton(ActionEvent event){
        if(cmb_options.getValue()==null){
            outputArea.appendText("Select an print option.\n");
            return;
        }
        int index = cmb_options.getSelectionModel().getSelectedIndex();
        switch (index) {
            case 0:
                PA_Command();
                break;
            case 1:
                PP_Command();
                break;
            case 2:
                PL_Command();
                break;
            case 3:
                PO_Command();
                break;
            case 4:
                PI_Command();
                break;
            case 5:
                PC_Command();
                break;
            case 6:
                PS_Command();
                break;
        }
    }

    /**
     * This method does the PA command. Print the list of appointments;
     * ordered by date/time/provider.
     */
    private void PA_Command(){
        if (!appointments.isEmpty()) {
            Sort.appointment(appointments, 'A');
            outputArea.appendText("\n** List of appointments, ordered by date/time/provider.\n");
            for (int i = 0; i < appointments.size(); i++) {
                outputArea.appendText(appointments.get(i).toString()+"\n");
            }
            outputArea.appendText("** end of list **\n");
        }else{
            outputArea.appendText("Schedule calendar is empty.\n");
        }
    }

    /**
     *This method does the PP command. Print the list of appointments;
     * ordered by patient/date/time.
     */
    private void PP_Command() {
        if (!appointments.isEmpty()) {
            Sort.appointment(appointments, 'P');
            outputArea.appendText("\n** List of appointments, ordered by patient/date/time.\n");
            for (int i = 0; i < appointments.size(); i++) {
                outputArea.appendText(appointments.get(i).toString()+"\n");
            }
            outputArea.appendText("** end of list **\n");
        }else{
            outputArea.appendText("Schedule calendar is empty.\n");
        }
    }

    /**
     * This method does the PL command. Print the list of appointments;
     * ordered by patient/date/time.
     */
    private void PL_Command() {
        if (!appointments.isEmpty()) {
            Sort.appointment(appointments, 'L');
            outputArea.appendText("\n** List of appointments, ordered by county/date/time.\n");
            for (int i = 0; i < appointments.size(); i++) {
                outputArea.appendText((appointments.get(i)).toString() +"\n");
            }
            outputArea.appendText("** end of list **\n");
        }else{
            outputArea.appendText("Schedule calendar is empty.\n");
        }
    }

    /**
     * This method does the PS command. Print the bill of the patients.
     */
    private void PS_Command(){
        if(appointments.isEmpty()) {
            outputArea.appendText("Schedule calendar is empty.\n");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Printing the billing statements for all patients will remove all appointments from the scheduler.");
        alert.setContentText("Do you want to proceed with the command?");

        // Customize the button types
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for the response
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // Execute the command only if "Yes" is selected
                Sort.appointment(appointments,'P');
                for(int i =0; i< appointments.size(); i++){
                    Appointment appointment = appointments.get(i);
                    Patient patient = new Patient(appointment.getPatient().getProfile(), appointment);
                    if(patients.isEmpty()){
                        patients.add(patient);
                    }else{
                        if(patients.contains(patient)){
                            int index = patients.indexOf(patient);
                            patients.get(index).addVisit(appointment);
                        }else{
                            patients.add(patient);
                        }
                    }
                }
                outputArea.appendText(("\n** Billing statement ordered by patient. **\n"));
                printChargePerPatient(patients);
                outputArea.appendText("** end of list **\n");
                appointments = new List<>();
            } else {
                outputArea.appendText("Operation canceled.\n");
            }
        });

    }

    /**
     * This print out the bill of each patient.
     * @param patients the patients list.
     */
    private void printChargePerPatient(List<Patient> patients){
        int[] charge = new int[patients.size()];
        for(int i =0; i< patients.size(); i++){
            Patient current = patients.get(i);
            charge[i] = current.charge();
        }
        for (int i = 0;i<charge.length; i++){
            DecimalFormat format = new DecimalFormat("#,###.00");
            String formatCharge = format.format(charge[i]);
            outputArea.appendText("(" + (i+1) +") " + patients.get(i).getProfile() +" [due: $" + formatCharge + "]\n");
        }
    }

    /**
     * This method does the PO command. Print the list of Office type appointments;
     * ordered by county/date/time.
     */
    private void PO_Command(){
        if (!appointments.isEmpty()) {
            Sort.appointment(appointments, 'O');
            outputArea.appendText("\n** List of office appointments ordered by county/date/time.\n");
            for (int i = 0; i < appointments.size(); i++) {
                if(appointments.get(i) instanceof Imaging) break;
                outputArea.appendText(appointments.get(i).toString()+"\n");
            }
            outputArea.appendText("** end of list **\n");
        }else{
            outputArea.appendText("Schedule calendar is empty.\n");
        }
    }

    /**
     * This method does the PI command. Print the list of imaging appointments;
     * ordered by county/date/time.
     */
    private void PI_Command(){
        if (!appointments.isEmpty()) {
            Sort.appointment(appointments, 'I');
            outputArea.appendText("\n** List of radiology appointments ordered by county/date/time.\n");
            for (int i = 0; i < appointments.size(); i++) {
                if(appointments.get(i) instanceof Imaging) {
                    outputArea.appendText(appointments.get(i).toString()+"\n");
                }
            }
            outputArea.appendText("** end of list **\n");
        }else{
            outputArea.appendText("Schedule calendar is empty.\n");
        }
    }

    /**
     * This method does the PC command. Print the list of expected credit amounts;
     * for providers for seeing patients, sorted by provider profile.
     */
    private void PC_Command(){
        if(appointments.isEmpty()){
            outputArea.appendText("Schedule calendar is empty.\n");
            return;
        }
        int[] charge = calc();
        outputArea.appendText("\n** Credit amount ordered by provider. **\n");
        for(int i = 0; i< providers.size(); i++){
            DecimalFormat format = new DecimalFormat("#,###.00");
            String formatCharge = format.format(charge[i]);
            outputArea.appendText("(" + (i+1) +") " + providers.get(i).getProfile() +" [credit amount: $" + formatCharge + "]\n");
        }
        outputArea.appendText("** end of list **\n");
    }

    /**
     * This method calculates the charge amount of providers.
     * @return return an int array with the charge for each provider in the order of the provider list.
     */
    private int[] calc(){
        Profile[] tempProvider = new Profile[providers.size()];
        for(int i = 0; i< providers.size(); i++){
            tempProvider[i] = providers.get(i).getProfile();
        }
        int[] money = new int[tempProvider.length];
        for(int i = 0; i< tempProvider.length; i++){
            Profile temp = tempProvider[i];
            Provider temp1 = providers.get(i);
            for(int j= 0; j< appointments.size(); j++){
                if(appointments.get(j).getProvider().getProfile().equals(temp)){
                    money[i] = money[i] +  temp1.rate();
                }
            }
        }
        return money;
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
    }

}
