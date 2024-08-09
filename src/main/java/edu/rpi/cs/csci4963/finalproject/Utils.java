package edu.rpi.cs.csci4963.finalproject;

import javafx.scene.control.Alert;

/**
 * Class containing static utility methods. Not intended to be instantiated.
 *
 * @version 1.0.0
 */
public class Utils {
    /**
     * Displays an error alert with the specified title and content.
     *
     * @param title   the title of the alert dialog
     * @param content the content message of the alert dialog
     */
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(content); // Set the content text
        alert.showAndWait(); // Show the alert and wait for user interaction
    }

    /**
     * Displays an information dialogue with the specified content.
     *
     * @param content the content message of the information dialogue
     */
    public static void showDialogue(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(null); // No title
        alert.setHeaderText(null); // No header text
        alert.setContentText(content); // Set the content text
        alert.showAndWait(); // Show the alert and wait for user interaction
    }
}
