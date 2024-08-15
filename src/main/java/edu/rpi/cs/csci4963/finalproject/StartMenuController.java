package edu.rpi.cs.csci4963.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static edu.rpi.cs.chane5.Utils.errorTrace;

public class StartMenuController {

    @FXML
    private ImageView backgroundImage;

    /**
     * Initializes the start menu controller.
     */
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

    @FXML
    private void onButtonMultiplayer(ActionEvent event) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("launcher-view.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Open Graph");
            stage.show();

            GameLauncherController controller = fxmlLoader.getController();
            // todo stuff
        } catch (Exception e) {
            errorTrace("invalid fxml path");
            e.printStackTrace();
        }
    }
}