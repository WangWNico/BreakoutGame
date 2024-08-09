package edu.rpi.cs.csci4963.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BreakoutApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(BreakoutApplication.class.getResource("start-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        scene.getStylesheets().add(BreakoutApplication.class.getResource("styles.css").toExternalForm()); // Load CSS
        stage.setTitle("Breakout Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}