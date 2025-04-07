package com.example.guestentryapp.controllers;

import com.example.guestentryapp.MedicalStatement;
import com.example.guestentryapp.db.Db;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class FormController {

    public void handleFormSubmission(DatePicker entryDate, HBox entryBox, HBox exitBox, TextField txtName,
                                     TextArea txtPurpose, Canvas signatureCanvas, CheckBox chkMedical,
                                     CheckBox chkInstruction) {

        // Sprawdzamy, czy pole "Posiadam badania lekarskie" jest zaznaczone
        if (!chkMedical.isSelected()) {
            // Jeśli nie, wyświetlamy oświadczenie
            showMedicalStatement();
        } else {
            // Walidacja formularza
            if (entryDate.getValue() == null ||
                    isTimeEmpty(entryBox) ||
                    isTimeEmpty(exitBox) ||
                    txtName.getText().isEmpty() ||
                    txtPurpose.getText().isEmpty() ||
                    isSignatureEmpty(signatureCanvas) ||
                    !chkInstruction.isSelected()) {

                // Wyświetlamy komunikat o błędzie
                showAlert("Wszystkie pola są wymagane. Proszę upewnić się, że wypełnione są: data, godziny wejścia i wyjścia, imię i nazwisko/firma, cel wizyty, podpis oraz zapoznanie się z instrukcją.");
            } else {
                // TODO: Obsługa zapisu danych
                System.out.println("Formularz wysłany!");
            }
        }
    }

    private boolean isTimeEmpty(HBox timeBox) {
        // Pobieramy godziny i minuty z ComboBoxów
        ComboBox<String> hourComboBox = (ComboBox<String>) timeBox.getChildren().get(0);
        ComboBox<String> minuteComboBox = (ComboBox<String>) timeBox.getChildren().get(2);

        // Sprawdzamy, czy godzina lub minuta są puste
        return hourComboBox.getValue() == null || minuteComboBox.getValue() == null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isSignatureEmpty(Canvas signatureCanvas) {
        Image image = signatureCanvas.snapshot(null, null);  // Zrób zrzut obrazu z canvasu
        javafx.scene.image.PixelReader pixelReader = image.getPixelReader();

        // Sprawdzamy wszystkie piksele na canvasie, czy zawierają kolor czarny
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = pixelReader.getColor(x, y);
                if (color.equals(Color.BLACK)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showMedicalStatement() {
        // Tworzymy nowe okno z oświadczeniem
        MedicalStatement medicalStatement = new MedicalStatement();
        medicalStatement.show();
    }
}
