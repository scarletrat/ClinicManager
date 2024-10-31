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
import util.List;
import util.Sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClinicManagerController {
    final static int REQUIRED_INPUTS = 7;
    private List<Appointment> appointments;
    private final List<Provider> providers = new List<>();
    private final List<Technician> technicians = new List<>();
    private CircularLinkedList rotation;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
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
     * An event handler registered with the 3 RadioButton objects.
     */
    void imagingSelected(ActionEvent event) {
        cmb_provider.setDisable(imagingApt.isSelected());
    }
}
