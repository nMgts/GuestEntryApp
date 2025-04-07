module com.example.guestentryapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.desktop;
    requires java.sql;

    opens com.example.guestentryapp to javafx.fxml;
    exports com.example.guestentryapp;
}