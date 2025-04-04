package com.example.guestentryapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartScreenController {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnAdmin;

    @FXML
    private void handleStart() {
        System.out.println("Start clicked");
        // Logika przejścia do następnego ekranu
    }

    @FXML
    private void handleAdmin() {
        System.out.println("Admin Panel clicked");
        // Logika przejścia do panelu administracyjnego
    }
}

