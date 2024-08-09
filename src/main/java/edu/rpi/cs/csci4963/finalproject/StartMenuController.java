package edu.rpi.cs.csci4963.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class StartMenuController {
    @FXML
    private void startGame(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(BreakoutApplication.class.getResource("breakout-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.show();
            scene.getRoot().requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}