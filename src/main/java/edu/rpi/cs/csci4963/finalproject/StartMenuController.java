package edu.rpi.cs.csci4963.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class StartMenuController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/edu/rpi/cs/csci4963/finalproject/main_menu.png"));
        backgroundImage.setImage(image);
    }

    @FXML
    private void startGame(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(BreakoutApplication.class.getResource("breakout-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.show();
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Width: " + newVal);
                scene.getRoot().requestFocus();
                stage.setWidth(newVal.doubleValue());
                scene.getWindow().sizeToScene();
            });
            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Length: " + newVal);
                scene.getRoot().requestFocus();
                stage.setWidth(newVal.doubleValue());
                scene.getWindow().sizeToScene();
            });
            scene.getRoot().requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}