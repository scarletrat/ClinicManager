module main.clinicmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens clinic to javafx.fxml;
    exports clinic;
}