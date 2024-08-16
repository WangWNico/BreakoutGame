package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.networking.connection.Server;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static edu.rpi.cs.chane5.Utils.*;

/**
 * The controller class for the game launcher.
 */
public class GameLauncherController {
    // singleton instance
    private static GameLauncherController gameLauncherController;

    // fields
    private static Scene scene;
    private final Stage stage;

    // FXML references
    @FXML
    private TextField textFieldHostPort;
    @FXML
    private TextField textFieldServerAddress;
    @FXML
    private TextField numInPortClient;

    /**
     * Creates the initial state of the launcher controller.
     */
    public GameLauncherController() {
        debug("constructed");
        if (gameLauncherController == null)
            gameLauncherController = this;
        else
            throw new RuntimeException("INSTANCE ALREADY EXISTS!");

        stage = new Stage();
        stage.setMinWidth(500);
        stage.setMinHeight(300);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Battleship Launcher");
    }

    /**
     * Gets the singleton instance of this launcher controller.
     *
     * @param pScene the scene constructed using an FXMLLoader of the launcher-view.fxml
     * @return the launcher instance
     */
    public static GameLauncherController get(Scene pScene) {
        debug("called");
        if (scene == null)
            scene = pScene;
        if (gameLauncherController != null)
            return gameLauncherController;

        errorTrace("Illegal state...");
        gameLauncherController = new GameLauncherController();
        return gameLauncherController;
    }

    /**
     * Gets the singleton instance of this launcher controller.
     *
     * @return the launcher instance
     */
    public static GameLauncherController get() {
        debug("called");
        return gameLauncherController;
    }

    /**
     * Once the GUI finishes constructing, do any logical and programmatic initialization generation as needed.
     * Expected to be called from the main JavaFX initialize method.
     * This method can only be called once per program, otherwise subsequent calls have no effects.
     */
    @FXML
    public void initialize() {
        if (gameLauncherController == null)
            gameLauncherController = this;

        // assign text field formats
        textFieldEnsurePositiveCountingNumber(textFieldHostPort, false);
        textFieldEnsurePositiveCountingNumber(numInPortClient, false);

        updateGUI();
    }

    // updates all default fields on the window
    private void updateGUI() {
        textFieldServerAddress.setText("localhost");
        numInPortClient.setText(String.valueOf(Server.DEFAULT_PORT));
        textFieldHostPort.setText(String.valueOf(Server.DEFAULT_PORT));
    }

    /**
     * Shows the Launcher window on the screen.
     *
     * @param utilWindow true if set as util window, false to set as normal window
     */
    public void show(boolean utilWindow) {
        debug("called");
        stage.hide();

        stage.setScene(scene);
        stage.show();
    }


    /**
     * Tries to convert the TextField input into a natural number <code>(0 < n)</code>. Alert the user if there is an invalid input.
     *
     * @param textField the JavaFX TextField to attempt parsing
     * @param name      the name of the input
     * @return the parsed number <code>n</code> or <code>-1</code> if there was an error.
     */
    private int parseTextFieldToNaturalNumber(TextField textField, String name) {
        debug("parseTextFieldToNaturalNumber > " + name + "\t\t" + textField.getText());
        try {
            if (textField.getText().isEmpty()) {
                alertError("Launcher - Server", "The '" + name + "' input can't be blank.");
                return -1;
            }

            int parsed = Integer.parseInt(textField.getText());
            if (0 < parsed)
                return parsed;

            alertError("Launcher - Server", "The '" + name + "' input must be greater than 0.");
            return -1;
        } catch (Exception e) {
            alertError("Launcher - Server", "The '" + name + "' input '" + textField.getText() + "' is not a number. Try again.");
            return -1;
        }
    }


    @FXML
    private void onButtonHost() {
        debug("called");
//        BattleshipEntry entry = BattleshipEntry.get();
        try {
//            entry.initServer(port);
        } catch (Exception e) {
            e.printStackTrace();
//            alertError("Launcher - Server", "There was an error in starting the server on port " + port + "\n" + e.getMessage());
            return;
        }

        // hides the launcher screen
        stage.hide();
    }

    @FXML
    private void onButtonJoin() {
        debug("called");

        String address = textFieldServerAddress.getText();
        int port = parseTextFieldToNaturalNumber(numInPortClient, "Server Port");

//        BattleshipEntry entry = BattleshipEntry.get();
        try {
//            entry.initClient(address, port);
        } catch (Exception e) {
            e.printStackTrace();
            alertError("Launcher - Client", "There was an error in connecting to " + address + ":" + port + "\n" + e.getMessage());
            return;
        }

        debug("reached");
        // todo update game state
//        entry.getJavaFxApp().show();
        stage.hide();
    }
}
