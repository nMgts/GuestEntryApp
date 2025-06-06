package com.example.guestentryapp;

import com.example.guestentryapp.controllers.FormController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;

public class MedicalStatement {
    private VBox statementLayout = new VBox(20);
    private double lastX, lastY;
    private int guestId;
    private String name;

    public void show() {
        Stage statementStage = new Stage();
        statementStage.setTitle("Oświadczenie o badaniach lekarskich");

        // Tworzenie elementów GUI
        //VBox statementLayout = new VBox(20);
        statementLayout.setStyle("-fx-padding: 20;");

        // Nagłówek
        Label lblHeader = new Label("OŚWIADCZENIE O STANIE ZDROWIA PRP 7.2");
        lblHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Pole imienia i nazwiska
        Label lblName = new Label("Imię i nazwisko / Firma:");
        TextField txtName = new TextField();
        txtName.setText(this.name);

        // Akapit 1
        Label lblStatement1 = new Label("Niniejszym oświadczam, iż nie posiadam żadnych silnych objawów chorobowych stanowiących " +
                "zagrożenie mikrobiologiczne dla środowiska pracy zakładu, będących podstawą do wystąpienia zakażenia krzyżowego wyrobów firmy.");

        // Akapit 2
        Label lblStatement2 = new Label("A. Oświadczam, iż w minionych trzytygodniowym okresie nie przechodziłem(łam) lub nie jestem w posiadaniu następujących chorób:" +
                "\n Zaznaczone oznacza, że nie przechodziłem lub nie jestem nosicielem");

        // Checkboxy
        CheckBox chkJaundice = new CheckBox("żółtaczka");
        CheckBox chkSalmonella = new CheckBox("zatrucie Salmonella");
        CheckBox chkAngina = new CheckBox("angina");
        CheckBox chkFlu = new CheckBox("grypa");
        CheckBox chkPneumonia = new CheckBox("zapalenie płuc");
        CheckBox chkTuberculosis = new CheckBox("gruźlica");
        CheckBox chkPusInfections = new CheckBox("ropne stany zapalne");
        CheckBox chkOther = new CheckBox("lub inne, będące w nosicielstwie");
        CheckBox othVir = new CheckBox("Nie jestem zakażony innymi wirusami np. pandemicznymi");

        // Informacja o objawach chorobowych
        Label lblSymptomInfo = new Label("W przypadku posiadania jakichkolwiek objawów chorobowych nie może Pan/Pani wejść do procesu produkcyjnego. Zapraszamy w innym terminie. " +
                "W przypadku ropnych stanów zapalnych skóry prosimy o nałożenie opatrunku (korzystając z naszej apteczki) i nałożenie jednorazowej rękawiczki.");
        lblSymptomInfo.setStyle("-fx-font-weight: bold;");

        // Akapit 3
        Label lblStatement3 = new Label("W ostatnich 2 tygodniach nie odwiedzałem(łam) / odwiedzałem(łam) kraje podwyższonego ryzyka " +
                "(np Chiny, Litwa, Afryka i inne). Jeżeli tak – wymień jakie:");

        // Checkbox "Nie odwiedzałem"
        CheckBox chkNoVisitRiskCountries = new CheckBox("Nie odwiedzałem(łam) krajów podwyższonego ryzyka");
        TextField txtVisitedCountries = new TextField();
        txtVisitedCountries.setPromptText("Wpisz kraje...");

        // Ukrywanie pola tekstowego, gdy checkbox "Nie odwiedzałem" jest zaznaczony
        chkNoVisitRiskCountries.setOnAction(e -> {
            if (chkNoVisitRiskCountries.isSelected()) {
                txtVisitedCountries.setDisable(true);
                txtVisitedCountries.setText("");
            } else {
                txtVisitedCountries.setDisable(false);
            }
        });

        // Akapit 4
        CheckBox chkcontact = new CheckBox("oraz nie posiadałem(łam) / posiadałem(łam) kontaktu z osobami posiadającymi choroby wymienione powyżej.");

        // Akapit 5
        Label lblStatement4 = new Label("C. Oświadczam, że w ostatnich dwóch tygodniach nie odwiedzałem(łam) / odwiedzałam(łem) zakładów " +
                "spożywczych wysokiego ryzyka (typu rzeźnie, ubojnie drobiu, chlewnie, fermy drobiu itp.). Jeżeli tak – wymień jakie:");

        // Checkboxy dla krajów
        CheckBox chkVisitedHighRisk = new CheckBox("Nie odwiedzałem(łam) zakładów wysokiego ryzyka");
        TextField txtVisitedHighRisk = new TextField();
        txtVisitedHighRisk.setPromptText("Wpisz zakłady...");

        chkVisitedHighRisk.setOnAction(e -> {
            if (chkVisitedHighRisk.isSelected()) {
                txtVisitedHighRisk.setDisable(true);
            } else {
                txtVisitedHighRisk.setDisable(false);
            }
        });

        // Mała, pogrubiona czcionka na końcu
        Label lblHighRiskInfo = new Label("W przypadku kontaktu z osobami chorymi lub przebywaniu w zakładach lub krajach wysokiego ryzyka a nie posiadaniu stanów chorobowych, " +
                "prosimy założyć maseczkę na twarz przez wejściem do procesu i nie dotykanie żadnych powierzchni roboczych maszyn/ urządzeń oraz surowców/wyrobów.");
        lblHighRiskInfo.setStyle("-fx-font-weight: bold;");

        // Podpis
        Label lblSignature = new Label("Podpis i data:");
        Canvas signatureCanvas = new Canvas(400, 200);
        GraphicsContext gc = signatureCanvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, signatureCanvas.getWidth(), signatureCanvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

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

        // Przycisk zatwierdzenia
        Button btnSubmit = new Button("Zatwierdź");
        btnSubmit.setOnAction(e -> {
            handleSubmit(txtName);
            statementStage.close();
        });


        // Dodanie elementów do layoutu
        statementLayout.getChildren().addAll(lblHeader, lblName, txtName, lblStatement1, lblStatement2,
                chkJaundice, chkSalmonella, chkAngina, chkFlu, chkPneumonia,
                chkTuberculosis, chkPusInfections, chkOther, othVir, lblSymptomInfo, lblStatement3,
                chkNoVisitRiskCountries, txtVisitedCountries, chkcontact, lblStatement4,
                chkVisitedHighRisk, txtVisitedHighRisk, lblHighRiskInfo, lblSignature, signatureCanvas, btnSubmit);

        // Tworzenie ScrollPane
        ScrollPane scrollPane = new ScrollPane(statementLayout);
        scrollPane.setFitToWidth(true);

        // Tworzenie sceny
        Scene statementScene = new Scene(scrollPane);

        // Ustawienie sceny i wyświetlenie okna
        statementStage.setScene(statementScene);
        statementStage.setMaximized(true);
        statementStage.show();
    }

    private void handleSubmit(TextField txtName) {
        // Wykonanie zrzutu VBox
        WritableImage image = statementLayout.snapshot(new SnapshotParameters(), null);

        String userHome = System.getProperty("user.home");
        File documentsDir = new File(userHome, "Documents/goscie");
        String networkPath = "Z:\\\\Norbert";

        // Sformatowanie nazwy pliku
        String name = txtName.getText().replaceAll("\\s+", "_"); // Zamiana spacji na podkreślenia
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%d_%s_%s.png", guestId + 1000, this.name, timestamp);

        // Utworzenie obiektu File z pełną ścieżką
        File file1 = new File(documentsDir, fileName);
        File file2 = new File(networkPath, fileName);

        try {
            // Zapis obrazu do pliku
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file1);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file2);

            FormController formController = new FormController();
            formController.updateMedicalStatement(guestId, guestId + 1000);

            // Wyświetlenie powiadomienia o zapisaniu pliku
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zapisano obraz");
            alert.setHeaderText(null);
            alert.setContentText("Obraz został zapisany w: " + file1.getAbsolutePath());
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Obsługa błędu zapisu pliku
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd zapisu");
            alert.setHeaderText(null);
            alert.setContentText("Error during saving file");
            alert.showAndWait();
        }
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
