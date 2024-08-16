package edu.rpi.cs.csci4963.finalproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static edu.rpi.cs.chane5.Utils.errorTrace;

/**
 * The StartMenuController class handles the interactions and logic for the start menu of the Breakout game.
 * It manages the start game button, background image, and transitions to other scenes.
 */
public class StartMenuController {
    @FXML
    private Button buttonStart;
    @FXML
    private ImageView backgroundImage;
    private BreakoutApplication breakoutApplication;
    private static StartMenuController startMenuController;

    /**
     * Gets the start menu controller.
     *
     * @return the start menu controller
     */
    public static StartMenuController getStartMenuController() {
        return startMenuController;
    }

    /**
     * Initializes the start menu controller by setting the background image and storing the controller instance.
     */
    @FXML
    private void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/edu/rpi/cs/csci4963/finalproject/main_menu.png"));
        backgroundImage.setImage(image);
        startMenuController = this;
    }

    /**
     * Starts the game by loading the Breakout game view and setting it as the current scene.
     *
     * @param event the ActionEvent triggered by the start button
     */
    @FXML
    private void startGame(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BreakoutApplication.class.getResource("breakout-view.fxml"));
            Parent fxml = fxmlLoader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(fxml);
            Stage stage = (Stage) scene.getWindow();
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.setMinWidth(850);
            stage.setMinHeight(1000);
            stage.show();
            scene.getRoot().requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the game remotely by firing the start button action.
     */
    public void remoteStartGame() {
        Platform.runLater(() -> buttonStart.fire());
    }

    /**
     * Handles the multiplayer button action by loading the multiplayer launcher view and displaying it in a new stage.
     *
     * @param event the ActionEvent triggered by the multiplayer button
     */
    @FXML
    private void onButtonMultiplayer(ActionEvent event) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("launcher-view.fxml"));
            Parent root = fxmlLoader.load();

            GameLauncherController gameLauncherController = fxmlLoader.getController();
            gameLauncherController.setApplicationInstance(breakoutApplication);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMinWidth(400);
            stage.setMinHeight(300);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Breakout Multiplayer");
            stage.show();
            gameLauncherController.setStageHide(stage);
        } catch (Exception e) {
            errorTrace("invalid fxml path");
            e.printStackTrace();
        }
    }

    /**
     * Sets the application instance.
     *
     * @param breakoutApplication the application instance
     */
    public void setApplicationInstance(BreakoutApplication breakoutApplication) {
        this.breakoutApplication = breakoutApplication;
    }
}