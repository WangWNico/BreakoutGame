package edu.rpi.cs.csci4963.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static final String VERSION = "1.0";
    private static boolean isJavaFXRunning = false;


    public static boolean isIsJavaFXRunning() {
        return isJavaFXRunning;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        isJavaFXRunning = true;
    }

    public static void main(String[] args) {
        launch();
    }
}