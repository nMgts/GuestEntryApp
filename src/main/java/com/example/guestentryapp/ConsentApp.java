package com.example.guestentryapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConsentApp extends Application {

    private double lastX, lastY; // do śledzenia pozycji kursora przy rysowaniu podpisu

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Aplikacja Zgód");

        // Ekran startowy z przyciskiem "Start"
        Button btnStart = new Button("Start");
        btnStart.setOnAction(e -> showForm(primaryStage));
        VBox startLayout = new VBox(20, btnStart);
        startLayout.setPadding(new Insets(20));
        startLayout.setStyle("-fx-alignment: center;");
        Scene startScene = new Scene(startLayout, 400, 300);

        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    private void showForm(Stage stage) {
        // Informacja o alergenach
        Label lblAllergens = new Label("Informacja o alergenach:\n- Orzechy\n- Mleko\n- Jaja");

        // Pola formularza
        TextField txtFirstName = new TextField();
        txtFirstName.setPromptText("Imię");
        TextField txtLastName = new TextField();
        txtLastName.setPromptText("Nazwisko");

        CheckBox cbAware = new CheckBox("Jestem świadomy");

        // Canvas do zbierania podpisu
        Canvas signatureCanvas = new Canvas(400, 200);
        GraphicsContext gc = signatureCanvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, signatureCanvas.getWidth(), signatureCanvas.getHeight());
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        // Obsługa rysowania podpisu na canvasie
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

        Button btnConfirm = new Button("Potwierdź");
        btnConfirm.setOnAction(e -> {
            // Walidacja pól – upewnij się, że imię, nazwisko są wpisane i checkbox zaznaczony
            if(txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() || !cbAware.isSelected()){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Proszę wypełnić wszystkie pola i zaznaczyć zgodę.");
                alert.show();
                return;
            }
            // Zapis danych do PDF
            try {
                saveDataAsPDF(txtFirstName.getText(), txtLastName.getText(), signatureCanvas);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Dane zapisane w pliku PDF.");
                alert.show();
            } catch (IOException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Błąd podczas zapisu PDF.");
                alert.show();
            }
        });

        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(20));
        formLayout.getChildren().addAll(lblAllergens, txtFirstName, txtLastName, cbAware, new Label("Podpis:"), signatureCanvas, btnConfirm);

        Scene formScene = new Scene(formLayout, 500, 600);
        stage.setScene(formScene);
    }

    private void saveDataAsPDF(String firstName, String lastName, Canvas signatureCanvas) throws IOException {
        // Zrzut canvasu z podpisem jako obraz
        WritableImage writableImage = new WritableImage((int)signatureCanvas.getWidth(), (int)signatureCanvas.getHeight());
        signatureCanvas.snapshot(null, writableImage);

        // Konwersja WritableImage na BufferedImage
        BufferedImage bufferedImage = new BufferedImage(
                (int) writableImage.getWidth(),
                (int) writableImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        PixelReader pixelReader = writableImage.getPixelReader();
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
            }
        }

        // Tymczasowy plik obrazka (png)
        File tempImageFile = File.createTempFile("signature", ".png");
        ImageIO.write(bufferedImage, "PNG", tempImageFile);

        // Utworzenie PDF przy użyciu PDFBox
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // 🔹 Poprawne ładowanie czcionki Unicode (DejaVu Sans) z zasobów
        InputStream fontStream = getClass().getResourceAsStream("/com/example/guestentryapp/dejavu-sans-ttf-2.37/dejavu-sans-ttf-2.37/ttf/DejaVuSans.ttf");
        if (fontStream == null) {
            throw new IOException("❌ Nie znaleziono czcionki w zasobach!");
        }
        PDType0Font font = PDType0Font.load(document, fontStream);

        // Dodanie nagłówka
        contentStream.beginText();
        contentStream.setFont(font, 18);
        contentStream.newLineAtOffset(50, 750);
        contentStream.showText("Dane zgody");
        contentStream.endText();

        // Dodanie danych z formularza
        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(50, 720);
        contentStream.showText("Imię: " + firstName);
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Nazwisko: " + lastName);
        contentStream.endText();

        // Wstawienie obrazu podpisu
        PDImageXObject pdImage = PDImageXObject.createFromFileByContent(tempImageFile, document);
        contentStream.drawImage(pdImage, 50, 450, 200, 100);

        contentStream.close();

        // Zapis PDF do katalogu domowego użytkownika
        String userHome = System.getProperty("user.home");

        // Pobranie aktualnej daty w formacie YYYY-MM-DD
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Tworzenie nazwy pliku: imię_nazwisko_YYYY-MM-DD.pdf
        String fileName = String.format("zgoda_%s_%s_%s.pdf", firstName, lastName, currentDate);

        // Zapis PDF do katalogu domowego użytkownika
        File pdfFile = new File(userHome, fileName);


        document.save(pdfFile);
        document.close();

        // Usunięcie tymczasowego pliku obrazka
        tempImageFile.delete();
    }


    private void loadFont(PDDocument document) throws IOException {
        // Ścieżka do czcionki w zasobach aplikacji
        String fontPath = "/com/example/guestentryapp/dejavu-sans-ttf-2.37/dejavu-sans-ttf-2.37/ttf/DejaVuSans.ttf";

        // Ładowanie czcionki z zasobów
        InputStream fontStream = getClass().getResourceAsStream(fontPath);

        if (fontStream == null) {
            throw new IOException("Nie znaleziono czcionki w zasobach: " + fontPath);
        }

        // Załaduj czcionkę z zasobów
        PDType0Font font = PDType0Font.load(document, fontStream);

        // Teraz czcionka jest gotowa do użycia w dokumencie PDF
    }
}
