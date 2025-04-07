module com.example.guestentryapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.guestentryapp to javafx.fxml;  // pozwala na refleksję w głównym pakiecie
    opens com.example.guestentryapp.controllers to javafx.fxml;  // pozwala na refleksję w pakiecie kontrolerów

    exports com.example.guestentryapp;
    exports com.example.guestentryapp.controllers;  // eksportowanie kontrolerów
}
