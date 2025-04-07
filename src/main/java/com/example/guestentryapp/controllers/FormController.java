package com.example.guestentryapp.controllers;

import com.example.guestentryapp.MedicalStatement;
import com.example.guestentryapp.db.Db;
import com.example.guestentryapp.models.Guest;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class FormController {
    private Db db = new Db();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void handleFormSubmission(DatePicker entryDate, HBox entryBox, HBox exitBox, TextField txtName,
                                     TextArea txtPurpose, Canvas signatureCanvas, CheckBox chkMedical,
                                     CheckBox chkInstruction) {

        if (entryDate.getValue() == null ||
                isTimeEmpty(entryBox) ||
                isTimeEmpty(exitBox) ||
                txtName.getText().isEmpty() ||
                txtPurpose.getText().isEmpty() ||
                isSignatureEmpty(signatureCanvas) ||
                !chkInstruction.isSelected()) {

            showAlert("Wszystkie pola są wymagane. Proszę upewnić się, że wypełnione są: data, godziny wejścia i wyjścia, imię i " +
                    "nazwisko/firma, cel wizyty, podpis oraz zapoznanie się z instrukcją.");

        } else {
            if (!chkMedical.isSelected()) {
                int guestId = getGuestId(txtName, entryDate);
                showMedicalStatement(guestId);
            }
            saveForm(entryDate, entryBox, exitBox, txtName, txtPurpose, signatureCanvas, chkMedical, chkInstruction);
            System.out.println("Formularz wysłany!");
        }
    }

    private void saveForm(DatePicker entryDate, HBox entryBox, HBox exitBox, TextField txtName,
                          TextArea txtPurpose, Canvas signatureCanvas, CheckBox chkMedical,
                          CheckBox chkInstruction) {
        LocalDate date = entryDate.getValue();
        String entryTime = getTimeFromBox(entryBox);
        String exitTime = getTimeFromBox(exitBox);
        String name = txtName.getText();
        String purpose = txtPurpose.getText();
        boolean medicalExams = chkMedical.isSelected();
        boolean instruction = chkInstruction.isSelected();
        byte[] signatureBytes = getSignatureBytes(signatureCanvas);

        db.insertGuest(date, entryTime, exitTime, name, purpose, medicalExams, instruction, signatureBytes);
    }

    public List<Guest> getGuests() {
        return db.getGuests();
    }

    public void updateMedicalStatement(int id, int medicalStatement) {
        db.updateMedicalStatement(id, medicalStatement);
    }

    private int getGuestId(TextField txtName, DatePicker entryDate) {
        return db.getGuestId(txtName.getText(), entryDate.getValue());
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

    private void showMedicalStatement(int guestId) {
        // Tworzymy nowe okno z oświadczeniem
        MedicalStatement medicalStatement = new MedicalStatement();
        medicalStatement.setGuestId(guestId);
        medicalStatement.show();
    }

    private String getTimeFromBox(HBox timeBox) {
        ComboBox<String> hourComboBox = (ComboBox<String>) timeBox.getChildren().get(0);
        ComboBox<String> minuteComboBox = (ComboBox<String>) timeBox.getChildren().get(2);

        String hour = hourComboBox.getValue();
        String minute = minuteComboBox.getValue();

        return hour + ":" + minute;
    }

    private byte[] getSignatureBytes(Canvas canvas) {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.warning("Cannot save signature: " + e.getMessage());
            return null;
        }
    }
}
