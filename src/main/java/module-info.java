module main.clinicmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens clinic to javafx.fxml;
    exports clinic;
}