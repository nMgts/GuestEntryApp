module com.example.guestentryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.guestentryapp to javafx.fxml;
    exports com.example.guestentryapp;
}