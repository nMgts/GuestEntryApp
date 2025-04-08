package com.example.guestentryapp;

import com.example.guestentryapp.controllers.FormController;
import com.example.guestentryapp.db.Db;
import com.example.guestentryapp.models.Guest;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.LocalDate;

public class GuestEntryApp extends Application {
    private Db db = new Db();
    private double lastX, lastY;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        db.getConnection();

        primaryStage.setTitle("Rejestr odwiedzająych zakład");

        // Przycisk Start
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

        btnStart.setOnAction(e -> showForm());

        // Przycisk Admin Panel
        Button btnAdminPanel = new Button("Admin Panel");
        btnAdminPanel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-padding: 15px 40px;" +
                        "-fx-background-color: linear-gradient(to right, #4CAF50, #81C784);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        );
        btnAdminPanel.setOnMouseEntered(e -> btnAdminPanel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-padding: 15px 40px;" +
                        "-fx-background-color: linear-gradient(to right, #388E3C, #66BB6A);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);"
        ));
        btnAdminPanel.setOnMouseExited(e -> btnAdminPanel.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-padding: 15px 40px;" +
                        "-fx-background-color: linear-gradient(to right, #4CAF50, #81C784);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
        ));

        btnAdminPanel.setOnAction(e -> showLoginDialog(primaryStage));

        // Layout przycisków
        VBox startLayout = new VBox(20, btnStart, btnAdminPanel);
        startLayout.setPadding(new Insets(20));
        startLayout.setStyle("-fx-alignment: center; -fx-background-color: #f4f4f4;");

        Scene startScene = new Scene(startLayout);
        primaryStage.setScene(startScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void showForm() {
        Stage formStage = new Stage();
        formStage.setTitle("Rejestracja odwiedzającego");

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
        CheckBox chkInstruction = new CheckBox("* Zapoznałem się z instrukcją dla odwiedzających proces produkcyjny");

        // Podpis
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

        FormController formController = new FormController();
        btnSubmit.setOnAction(e -> {
            formController.handleFormSubmission(entryDate, entryBox, exitBox, txtName, txtPurpose, signatureCanvas, chkMedical, chkInstruction);
            formStage.close();
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
        formGrid.add(chkInstruction, 1, 6);
        formGrid.add(lblSignature, 0, 7);
        formGrid.add(signatureCanvas, 1, 7);
        formGrid.add(btnSubmit, 1, 8);

        VBox formLayout = new VBox(20, formGrid);
        formLayout.setPadding(new Insets(20));
        formLayout.setStyle("-fx-alignment: center; -fx-background-color: #f4f4f4;");

        formLayout.setMinWidth(Region.USE_PREF_SIZE);
        formLayout.setMinHeight(Region.USE_PREF_SIZE);
        formLayout.prefWidthProperty().bind(formStage.widthProperty());
        formLayout.prefHeightProperty().bind(formStage.heightProperty());

        formGrid.setAlignment(Pos.CENTER);

        Scene formScene = new Scene(formLayout);
        formStage.setScene(formScene);

        formStage.setMaximized(true);
        formStage.show();
    }

    // Metoda do pokazania okna logowania
    private void showLoginDialog(Stage primaryStage) {
        // Tworzymy okno logowania
        Stage loginStage = new Stage();
        loginStage.setTitle("Logowanie do Panelu Administracyjnego");

        // Tworzymy pola do wprowadzenia hasła
        Label lblPassword = new Label("Wprowadź hasło:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Hasło");

        Button btnLogin = new Button("Zaloguj");
        btnLogin.setOnAction(e -> {
            if (passwordField.getText().equals("85!E")) {
                // Hasło poprawne, otwieramy panel administracyjny
                loginStage.close();
                showAdminPanel(primaryStage);
            } else {
                // Hasło błędne, wyświetlamy komunikat
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd logowania");
                alert.setHeaderText(null);
                alert.setContentText("Niepoprawne hasło.");
                alert.showAndWait();
            }
        });

        Button btnCancel = new Button("Anuluj");
        btnCancel.setOnAction(e -> loginStage.close());

        VBox loginLayout = new VBox(10, lblPassword, passwordField, btnLogin, btnCancel);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setStyle("-fx-alignment: center; -fx-background-color: #f4f4f4;");

        Scene loginScene = new Scene(loginLayout, 300, 200);
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    // Nowa metoda do wyświetlania panelu administracyjnego
    private void showAdminPanel(Stage primaryStage) {
        // Tworzymy nowe okno (panel administracyjny)
        Stage adminPanelStage = new Stage();
        adminPanelStage.setTitle("Panel administracyjny");

        //Tabela
        TableView<Guest> guestTable = new TableView<>();
        guestTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //Kolumny
        TableColumn<Guest, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Guest, LocalDate> colDate = new TableColumn<>("Data");
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Guest, String> colEntry = new TableColumn<>("Godzina wejścia");
        colEntry.setCellValueFactory(new PropertyValueFactory<>("entryTime"));

        TableColumn<Guest, String> colExit = new TableColumn<>("Godzina wyjścia");
        colExit.setCellValueFactory(new PropertyValueFactory<>("exitTime"));

        TableColumn<Guest, String> colName = new TableColumn<>("Imię i nazwisko / Firma");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Guest, String> colPurpose = new TableColumn<>("Cel wizyty");
        colPurpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));

        TableColumn<Guest, Boolean> colMedical = new TableColumn<>("Badania lekarskie");
        colMedical.setCellValueFactory(new PropertyValueFactory<>("medicalExams"));
        colMedical.setCellFactory(param -> new TableCell<Guest, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Tak" : "Nie");
                }
            }
        });

        TableColumn<Guest, Integer> colMedStat = new TableColumn<>("Oświadczenie o stanie zdrowia nr");
        colMedStat.setCellValueFactory(new PropertyValueFactory<>("medicalStatement"));
        colMedStat.setCellFactory(param -> new TableCell<Guest, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item == 0) {
                    setText("");
                } else {
                    setText(String.valueOf(item));
                }
            }
        });

        TableColumn<Guest, Boolean> colInstruction = new TableColumn<>("Oświadczam, że zapoznałem się z instrukcją dla osób odwiedzających proces produkcyjny");
        colInstruction.setCellValueFactory(new PropertyValueFactory<>("instructionStatement"));
        colInstruction.setCellFactory(param -> new TableCell<Guest, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Tak" : "Nie");
                }
            }
        });

        // Kolumna podpisu
        TableColumn<Guest, byte[]> colSignature = new TableColumn<>("Podpis");
        colSignature.setCellValueFactory(new PropertyValueFactory<>("signature"));

        // Ustawienie renderera dla podpisu (byte[] -> Image)
        colSignature.setCellFactory(param -> new TableCell<Guest, byte[]>() {
            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        // Konwersja byte[] na Image
                        Image signatureImage = new Image(new ByteArrayInputStream(item));
                        ImageView imageView = new ImageView(signatureImage);
                        imageView.setFitWidth(100);  // Ustawienie szerokości obrazu
                        imageView.setPreserveRatio(true);  // Zachowanie proporcji obrazu
                        setGraphic(imageView);
                        setStyle("-fx-alignment: center;");
                    } catch (Exception e) {
                        setGraphic(null);  // Jeśli nie udało się załadować obrazu, pokazujemy puste miejsce
                    }
                }
            }
        });

        guestTable.getColumns().addAll(colId, colDate, colEntry, colExit, colName, colPurpose,
                colMedical, colMedStat, colInstruction, colSignature);
        guestTable.setRowFactory(tv -> {
            TableRow<Guest> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                System.out.println("klik");
                if (!row.isEmpty()) {  // event.getClickCount() == 2 &&
                    Guest selectedGuest = row.getItem();
                    int statementId = selectedGuest.getMedicalStatement();

                    if (statementId > 0) {
                        showMedicalStatementImage(statementId);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Brak oświadczenia");
                        alert.setHeaderText(null);
                        alert.setContentText("Ten gość nie ma przypisanego numeru oświadczenia.");
                        alert.showAndWait();
                    }
                }
            });
            return row;
        });

        // Wczytanie danych z bazy
        FormController formController = new FormController();
        guestTable.setItems(FXCollections.observableArrayList(formController.getGuests()));

        // Przycisk zamknij
        Button btnCloseAdmin = new Button("Zamknij");
        btnCloseAdmin.setOnAction(e -> adminPanelStage.close());

        // Przycisk odśwież
        Button btnRefresh = new Button("Odśwież");
        btnRefresh.setOnAction(e -> {
            guestTable.setItems(FXCollections.observableArrayList(formController.getGuests()));
            guestTable.refresh();
            });

        // Layout
        VBox adminLayout = new VBox(20, guestTable, btnCloseAdmin, btnRefresh);
        adminLayout.setPadding(new Insets(20));
        adminLayout.setStyle("-fx-background-color: #f4f4f4;");

        // Obliczanie 3/4 wysokości ekranu
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double height = screenBounds.getHeight() * 0.75;  // 3/4 wysokości ekranu

        // Ustawienie wysokości tabeli na 3/4 wysokości ekranu
        guestTable.setPrefHeight(height);  // Ustawiamy preferowaną wysokość tabeli

        // Ustawienie rozmiaru okna
        adminPanelStage.setHeight(height + 100);

        Scene adminScene = new Scene(adminLayout);
        adminPanelStage.setScene(adminScene);
        adminPanelStage.setMaximized(true);
        adminPanelStage.show();
    }

    private void showMedicalStatementImage(int statementId) {
        // Format nazwy pliku (szukamy po prefiksie "ID_" + statementId)
        String filePrefix = statementId + "_";
        String userHome = System.getProperty("user.home");
        File documentsDir = new File(userHome, "Documents/goscie");
        File networkDir = new File("Z:\\Norbert");

        File imageFile = findImageFileByPrefix(documentsDir, filePrefix);
        if (imageFile == null) {
            imageFile = findImageFileByPrefix(networkDir, filePrefix);
        }

        if (imageFile != null && imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());

            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            final double[] zoomFactor = {1.0};
            imageView.setOnScroll(event -> {
                if (event.getDeltaY() > 0) {
                    zoomFactor[0] *= 1.1;
                } else {
                    zoomFactor[0] /= 1.1;
                }
                imageView.setScaleX(zoomFactor[0]);
                imageView.setScaleY(zoomFactor[0]);
            });

            ScrollPane scrollPane = new ScrollPane(imageView);
            scrollPane.setPannable(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            // Layout główny
            VBox layout = new VBox(scrollPane);
            layout.setPadding(new Insets(10));
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);

            Stage imageStage = new Stage();
            imageStage.setTitle("Oświadczenie nr " + statementId);
            imageStage.setScene(scene);
            imageStage.initModality(Modality.APPLICATION_MODAL);
            imageStage.setMaximized(true); // pełny ekran
            imageStage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Plik nie znaleziony");
            alert.setHeaderText(null);
            alert.setContentText("Nie znaleziono pliku dla oświadczenia nr " + statementId);
            alert.showAndWait();
        }
    }

    private File findImageFileByPrefix(File directory, String prefix) {
        File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(prefix) && name.endsWith(".png"));
        return (matchingFiles != null && matchingFiles.length > 0) ? matchingFiles[0] : null;
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
}
