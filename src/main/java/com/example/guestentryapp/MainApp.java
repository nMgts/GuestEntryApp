package com.example.guestentryapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/guestentryapp/fxml/StartScreen.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Rejestr odwiedzających zakład");
        Scene scene = new Scene(root);
        // Ładowanie specyficznego pliku CSS dla tego widoku
        scene.getStylesheets().add(getClass().getResource("/com/example/guestentryapp/css/StartScreen.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
