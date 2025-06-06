module com.example.guestentryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires javafx.swing;
    requires org.apache.poi.ooxml;

    opens com.example.guestentryapp to javafx.fxml;
    opens com.example.guestentryapp.models to javafx.base;
    exports com.example.guestentryapp;
}