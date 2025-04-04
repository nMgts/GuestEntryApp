package com.example.guestentryapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

import java.time.LocalDate;

public class GuestEntryApp extends Application {

    private double lastX, lastY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Rejestr odwiedzająych zakład");

        Button btnStart = new Button("Start");
        btnStart.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-padding: 15px 40px;" +
                        "-fx-background-color: linear-gradient(to right, #ff7e5f, #feb47b);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );
        btnStart.setOnMouseEntered(e -> btnStart.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-padding: 15px 40px;" +
                        "-fx-background-color: linear-gradient(to right, #ff6a00, #ee0979);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);"
        ));
        btnStart.setOnMouseExited(e -> btnStart.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-padding: 15px 40px;" +
                        "-fx-background-color: linear-gradient(to right, #ff7e5f, #feb47b);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        ));

        btnStart.setOnAction(e -> showForm(primaryStage));

        VBox startLayout = new VBox(20, btnStart);
        startLayout.setPadding(new Insets(20));
        startLayout.setStyle("-fx-alignment: center; -fx-background-color: #f4f4f4;");

        Scene startScene = new Scene(startLayout);
        primaryStage.setScene(startScene);

        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    private void showForm(Stage primaryStage) {
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        // Data wejścia
        Label lblEntryDate = new Label("Data wejścia:");
        DatePicker entryDate = new DatePicker(LocalDate.now());

        // Godzina wejścia
        Label lblEntryTime = new Label("Godzina wejścia:");
        HBox entryBox = createTimeSelectionBox();

        // Godzina wyjścia
        Label lblExitTime = new Label("Godzina wyjścia:");
        HBox exitBox = createTimeSelectionBox();

        // Imię i nazwisko lub firma
        Label lblName = new Label("Imię i nazwisko / Firma:");
        TextField txtName = new TextField();

        // Cel wizyty
        Label lblPurpose = new Label("Cel wizyty:");
        TextArea txtPurpose = new TextArea();
        txtPurpose.setPrefRowCount(3);

        // Checkboxy
        CheckBox chkMedical = new CheckBox("Posiadam badania lekarskie");
        CheckBox chkHealthDeclaration = new CheckBox("Oświadczenie o stanie zdrowia");
        CheckBox chkInstruction = new CheckBox("* Zapoznałem się z instrukcją dla odwiedzających proces produkcyjny");

        Label lblSignature = new Label("Podpis:");
        Canvas signatureCanvas = new Canvas(400, 200);
        GraphicsContext gc = signatureCanvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, signatureCanvas.getWidth(), signatureCanvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Obsługa rysowania podpisu
        signatureCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        signatureCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double x = e.getX();
            double y = e.getY();
            gc.strokeLine(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        });

        // Przycisk zatwierdzający
        Button btnSubmit = new Button("Zatwierdź");
        btnSubmit.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;"
        );
        btnSubmit.setOnMouseEntered(e -> btnSubmit.setStyle("-fx-background-color: #45a049;"));
        btnSubmit.setOnMouseExited(e -> btnSubmit.setStyle("-fx-background-color: #4CAF50;"));

        btnSubmit.setOnAction(e -> {
            // walidacja przenieść do innej metody
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
        });

        // Układ formularza
        formGrid.add(lblEntryDate, 0, 0);
        formGrid.add(entryDate, 1, 0);
        formGrid.add(lblEntryTime, 0, 1);
        formGrid.add(entryBox, 1, 1);
        formGrid.add(lblExitTime, 0, 2);
        formGrid.add(exitBox, 1, 2);
        formGrid.add(lblName, 0, 3);
        formGrid.add(txtName, 1, 3);
        formGrid.add(lblPurpose, 0, 4);
        formGrid.add(txtPurpose, 1, 4);
        formGrid.add(chkMedical, 1, 5);
        formGrid.add(chkHealthDeclaration, 1, 6);
        formGrid.add(chkInstruction, 1, 7);
        formGrid.add(lblSignature, 0, 8);
        formGrid.add(signatureCanvas, 1, 8);
        formGrid.add(btnSubmit, 1, 9);

        VBox formLayout = new VBox(20, formGrid);
        formLayout.setPadding(new Insets(20));
        formLayout.setStyle("-fx-alignment: center; -fx-background-color: #f4f4f4;");

        formLayout.setMinWidth(Region.USE_PREF_SIZE);
        formLayout.setMinHeight(Region.USE_PREF_SIZE);
        formLayout.prefWidthProperty().bind(primaryStage.widthProperty());
        formLayout.prefHeightProperty().bind(primaryStage.heightProperty());

        formGrid.setAlignment(Pos.CENTER);

        Scene formScene = new Scene(formLayout);

        primaryStage.setMaximized(true);
        primaryStage.setScene(formScene);
    }

    private HBox createTimeSelectionBox() {
        // Tworzenie ComboBox dla godzin
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            hours.add(String.format("%02d", i));
        }
        ComboBox<String> hourComboBox = new ComboBox<>(hours);
        hourComboBox.setPromptText("Godzina");
        hourComboBox.setPrefWidth(80);

        // Tworzenie ComboBox dla minut
        ObservableList<String> minutes = FXCollections.observableArrayList();
        for (int i = 0; i < 60; i += 5) {
            minutes.add(String.format("%02d", i));
        }
        ComboBox<String> minuteComboBox = new ComboBox<>(minutes);
        minuteComboBox.setPromptText("Minuta");
        minuteComboBox.setPrefWidth(80);

        // Układ poziomy dla godzin i minut
        HBox timeBox = new HBox(10, hourComboBox, new Label(":"), minuteComboBox);
        timeBox.setAlignment(Pos.CENTER_LEFT);

        return timeBox;
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
        Image image = signatureCanvas.snapshot(null, null);
        javafx.scene.image.PixelReader pixelReader = image.getPixelReader();

        // Sprawdzanie piksela na początku, czy jest koloru LIGHTGRAY
        Color color = pixelReader.getColor(0, 0);
        return color.equals(Color.LIGHTGRAY);
    }
}
