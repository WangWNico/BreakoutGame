package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BreakoutApplication extends Application {
    private static boolean isJavaFxRunning = false;

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--debug".equals(args[i]))
                Utils.DEBUG_MODE = true;
        }

        launch();
    }

    public static boolean isJavaFXRunning() {
        return isJavaFxRunning;
    }

    @Override
    public void start(Stage stage) throws Exception {
        isJavaFxRunning = true;
        FXMLLoader fxmlLoader = new FXMLLoader(BreakoutApplication.class.getResource("start-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
        scene.getStylesheets().add(BreakoutApplication.class.getResource("styles.css").toExternalForm()); // Load CSS
        stage.setTitle("Breakout Game");
        stage.setScene(scene);
        stage.show();
    }
}