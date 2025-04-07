package com.example.guestentryapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class FormController {

    @FXML
    private DatePicker entryDate;

    @FXML
    private HBox entryBox;

    @FXML
    private HBox exitBox;

    @FXML
    private TextField txtName;

    @FXML
    private TextArea txtPurpose;

    @FXML
    private CheckBox chkMedical;

    @FXML
    private CheckBox chkHealthDeclaration;

    @FXML
    private CheckBox chkInstruction;

    @FXML
    private Canvas signatureCanvas;

    @FXML
    private void handleSubmit(ActionEvent event) {
        if (entryDate.getValue() == null ||
                isTimeEmpty(entryBox) ||
                isTimeEmpty(exitBox) ||
                txtName.getText().isEmpty() ||
                txtPurpose.getText().isEmpty() ||
                isSignatureEmpty(signatureCanvas) ||
                !chkInstruction.isSelected()) {

            // Wyświetl komunikat o błędzie
            showAlert("Wszystkie pola są wymagane. Proszę upewnić się, że wypełnione są: data, godziny wejścia i wyjścia, imię i nazwisko/firma, cel wizyty, podpis oraz zapoznanie się z instrukcją.");
        } else {
            // TODO: Obsługa zapisu danych
            System.out.println("Formularz wysłany!");
        }
    }

    private boolean isTimeEmpty(HBox timeBox) {
        ComboBox<String> hourComboBox = (ComboBox<String>) timeBox.getChildren().get(0);
        ComboBox<String> minuteComboBox = (ComboBox<String>) timeBox.getChildren().get(2);

        return hourComboBox.getValue() == null || minuteComboBox.getValue() == null;
    }

    private boolean isSignatureEmpty(Canvas signatureCanvas) {
        Image image = signatureCanvas.snapshot(null, null);
        PixelReader pixelReader = image.getPixelReader();
        Color color = pixelReader.getColor(0, 0);
        return color.equals(Color.LIGHTGRAY);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        entryDate.setValue(LocalDate.now());
        createTimeSelectionBox(entryBox);
        createTimeSelectionBox(exitBox);
    }

    private void createTimeSelectionBox(HBox timeBox) {
        ComboBox<String> hourComboBox = new ComboBox<>();
        ComboBox<String> minuteComboBox = new ComboBox<>();
        // Uzupełnij ComboBox o godziny i minuty
        timeBox.getChildren().addAll(hourComboBox, new javafx.scene.control.Label(":"), minuteComboBox);
    }
}
