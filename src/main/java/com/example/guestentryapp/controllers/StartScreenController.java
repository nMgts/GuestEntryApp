package com.example.guestentryapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class StartScreenController {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnAdmin;

    @FXML
    private void handleStart() throws Exception {
        // Załaduj nową scenę z formularzem
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/guestentryapp/fxml/form.fxml"));
        StackPane formRoot = loader.load();
        Scene formScene = new Scene(formRoot, 800, 600);

        // Pobierz obecny Stage i zmień scenę
        Stage currentStage = (Stage) btnStart.getScene().getWindow();
        currentStage.setScene(formScene);
    }

    @FXML
    private void handleAdmin() {
        System.out.println("Admin Panel clicked");
        // Logika przejścia do panelu administracyjnego
    }
}

