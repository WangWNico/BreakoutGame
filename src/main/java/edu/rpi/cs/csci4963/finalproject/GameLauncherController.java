package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.networking.connection.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static edu.rpi.cs.chane5.Utils.*;

/**
 * The controller class for the game launcher.
 */
public class GameLauncherController {
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
    private BreakoutApplication breakoutApplication;
    private Runnable cb;
    private Stage stageToHide;

    /**
     * Creates the initial state of the launcher controller.
     */
    public GameLauncherController() {
        debug("constructed");
        stage = new Stage();
        stage.setMinWidth(500);
        stage.setMinHeight(300);
        stage.initStyle(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Breakout Launcher");
    }

    /**
     * Once the GUI finishes constructing, do any logical and programmatic initialization generation as needed.
     * Expected to be called from the main JavaFX initialize method.
     * This method can only be called once per program, otherwise subsequent calls have no effects.
     */
    @FXML
    private void initialize() {
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
    private void onButtonHost(ActionEvent event) {
        debug("called: attempting host on port " + textFieldHostPort.getText());

        int port = parseTextFieldToNaturalNumber(textFieldHostPort, "port");

        try {
            stageToHide.hide();
            breakoutApplication.initServer(port);
        } catch (Exception e) {
            e.printStackTrace();
            alertError("Launcher - Server", "There was an error in starting the server on port " + port + "\n" + e.getMessage());
            return;
        }
    }

    @FXML
    private void onButtonJoin() {
        debug("called: joining on " + textFieldServerAddress.getText() + ":" + numInPortClient.getText());


        String server = textFieldServerAddress.getText();
        int port = parseTextFieldToNaturalNumber(numInPortClient, "port");

        if (server.isEmpty()) {
            alertError("Launcher - Join", "Server IP Address can't be blank");
            return;
        }

        stageToHide.close();

        try {
            breakoutApplication.initClient(server, port);
        } catch (Exception e) {
            e.printStackTrace();
            alertError("Launcher - Server", "There was an error in starting the server on port " + port + "\n" + e.getMessage());
        }
    }

    /**
     * Sets the application instance
     *
     * @param breakoutApplication the application instance
     */
    public void setApplicationInstance(BreakoutApplication breakoutApplication) {
        this.breakoutApplication = breakoutApplication;
    }

    public void setStageHide(Stage stage) {
        this.stageToHide = stage;
    }
}
