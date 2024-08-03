package edu.rpi.cs.chane5;

import edu.rpi.cs.csci4963.finalproject.HelloApplication;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Class containing static utility methods. Not intended to be instantiated.
 *
 * @version 1.0.0
 */
public class Utils {
    private static final String REGEX_MATCH_ALL_NON_DIGITS = "[^\\d]";
    private static final String REGEX_MATCH_ALL_LEADING_ZEROS = "^0*";
    private static final String EMPTY = "";
    /**
     * Debug mode flag, set using the program CLI args.
     * If true it will print a lot of verbose information about the current state of the program.
     * Otherwise, all the verbose outputs will not be displayed to the terminal.
     */
    public static boolean DEBUG_MODE = false;
    private static boolean DEBUG = true;

    private Utils() {
        errorTrace("illegal instantiation");
    }


    /**
     * Creates a label with the msg wrapped around the label box every 50 chars.
     *
     * @param msg the message to wrap
     * @return a label with the msg wrapped
     */
    private static Label buildWrappedStringLabel(String msg) {
        final int WRAP_LENGTH = 50;

        // shortcut
        if (msg.length() < WRAP_LENGTH)
            return new Label(msg);

        // build modified string
        StringBuilder builder = new StringBuilder(msg);
        boolean breakNow = false;
        int lineLength = 0;

        for (int i = 1; i + 1 < builder.length(); i++) {
            lineLength++;

            if (builder.charAt(i) == '\n') {
                // reset the breaker if a newline char is encountered
                lineLength = 1;
                breakNow = false;
                continue;
            }

            // if line length is 50 chars with no previous break, find next SPACE to replace with newline
            breakNow = breakNow || (lineLength % WRAP_LENGTH == 0);
            if (breakNow && builder.charAt(i) == ' ') {
                builder.replace(i, i + 1, "\n");
                breakNow = false;
            }
        }

        return new Label(builder.toString());
    }

    /**
     * Prints a debug trace method if DEBUG flag is enabled.
     * <br />
     * Outputs in the format of the caller's class and method then the message on a new line.
     * {@code CallerClassName █ callerMethodName @ LineNumber \n \t > msg }
     *
     * @param msg The message to print to the console.
     */
    public static void debug(String msg) {
        if (!DEBUG_MODE)
            return;
        // notice, requires Java 9
        StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
        StackWalker.StackFrame parent = stackWalker.walk(stream -> stream.skip(1).findFirst().get());
        String className = parent.getClassName();
        System.out.printf("%s █ %s @ %d\n\t> %s\n", className.substring(className.lastIndexOf(".") + 1), parent.getMethodName(), parent.getLineNumber(), msg);
    }

    /**
     * Prints an error trace method if DEBUG flag is enabled in the color RED and a stack trace is provided.
     * <br />
     * Outputs in the format of stack trace then the caller's class and method then the message on a new line.
     * {@code CallerClassName █ callerMethodName @ LineNumber \n \t > msg }
     *
     * @param error The message to print to the console.
     */
    public static void error(String error) {
        System.out.print("\u001B[31m");
        debug(error);
        System.out.print("\u001B[0m");
    }

    /**
     * Prints an error trace method if DEBUG flag is enabled in the color RED.
     * <br />
     * Outputs in the format of the caller's class and method then the message on a new line.
     * {@code STACK_TRACE_HERE \n CallerClassName █ callerMethodName @ LineNumber \n \t > msg }
     *
     * @param error The message to print to the console.
     */
    public static void errorTrace(String error) {
        System.out.print("\u001B[31m");
        Arrays.stream(Thread.currentThread().getStackTrace()).toList().forEach(System.out::println);
        debug(error);
        System.out.print("\u001B[0m");

    }

    /**
     * Creates an alert popout with a header and body.
     *
     * @param header the general type of error
     * @param msg    detailed information about the error
     * @param cb     the callback function after the user presses a button
     */
    public static void alertError(String header, String msg, Consumer<? super ButtonType> cb) {
        if (!HelloApplication.isIsJavaFXRunning())
            return;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.getDialogPane().setContent(buildWrappedStringLabel(msg));
            alert.showAndWait().ifPresent(cb);
        });
    }

    /**
     * Creates an alert popout with a header and body.
     *
     * @param header the general type of error
     * @param msg    detailed information about the error
     */
    public static void alertError(String header, String msg) {
        if (HelloApplication.isIsJavaFXRunning())
            alertError(header, msg, (buttonType -> {
            }));
    }

    /**
     * Creates a warning popout with a header and body.
     *
     * @param header the general type of error
     * @param msg    detailed information about the error
     * @param cb     the method to run when the OK button is pressed
     */
    public static void alertWarning(String header, String msg, Consumer<? super ButtonType> cb) {
        if (!HelloApplication.isIsJavaFXRunning())
            return;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(header);
            alert.getDialogPane().setContent(buildWrappedStringLabel(msg));
            alert.showAndWait().ifPresent(cb);
        });
    }

    /**
     * Creates a confirmation popout with a header and body.
     *
     * @param header    the general type of error
     * @param msg       detailed information about the error
     * @param confirm   the consumer function after the user presses OK
     * @param otherwise the consumer function after the user doesn't select OK, such as CANCEL or closing the window
     */
    public static void alertConfirmation(String header, String msg, Runnable confirm, Runnable otherwise) {
        if (!HelloApplication.isIsJavaFXRunning())
            return;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning");
            alert.setHeaderText(header);
            alert.getDialogPane().setContent(buildWrappedStringLabel(msg));
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK)
                    confirm.run();
                else
                    otherwise.run();
            });
        });
    }

    /**
     * Creates a confirmation popout with a header and body.
     *
     * @param header  the general type of error
     * @param msg     detailed information about the error
     * @param confirm the consumer function after the user presses OK
     */
    public static void alertConfirmation(String header, String msg, Runnable confirm) {
        if (HelloApplication.isIsJavaFXRunning())
            alertConfirmation(header, msg, confirm, () -> {
            });
    }

    /**
     * Creates a confirmation popout with a header and body.
     *
     * @param header the general type of error
     * @param msg    detailed information about the error
     */
    public static void alertConfirmation(String header, String msg) {
        if (HelloApplication.isIsJavaFXRunning())
            alertConfirmation(header, msg, () -> {
            }, () -> {
            });
    }

    /**
     * Creates an information popout with a header and body.
     *
     * @param header the general type of error
     * @param msg    detailed information about the error
     * @param cb     the consumer function after the user presses OK
     */
    public static void alertInfo(String header, String msg, Consumer<? super ButtonType> cb) {
        if (!HelloApplication.isIsJavaFXRunning())
            return;
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText(header);
            alert.getDialogPane().setContent(buildWrappedStringLabel(msg));
            alert.showAndWait().ifPresent(cb);
        });
    }

    /**
     * Creates an information popout with a header and body.
     *
     * @param header the general type of error
     * @param msg    detailed information about the error
     */
    public static void alertInfo(String header, String msg) {
        if (HelloApplication.isIsJavaFXRunning())
            alertInfo(header, msg, buttonType -> {
            });
    }

    /**
     * Adds a listener to the JavaFX TextField to conform all inputs to be digits and not start with a leading 0.
     * Notice negative numbers are not supported.
     *
     * @param textField the TextField to add this constraint to
     * @param allowZero enable 0 as a valid input
     */
    public static void textFieldEnsurePositiveCountingNumber(TextField textField, boolean allowZero) {
        textField.getProperties().put("allowZero", allowZero);

        // conform text fields to only numbers and no leading zeros
        textField.textProperty().addListener(((observableValue, oldString, newString) -> {
            boolean zero = (Boolean) textField.getProperties().get("allowZero");
            if (zero && ("0".equals(newString) || "00".equals(newString))) {
                // allows for singular 0 input, avoids double Zero "Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: The start must be <= the end"
                textField.setText("0");
                return;
            }

            textField.setText(newString.replaceAll(REGEX_MATCH_ALL_NON_DIGITS, EMPTY).replaceFirst(REGEX_MATCH_ALL_LEADING_ZEROS, EMPTY));
        }));
    }

    /**
     * Opens a dialog for the user to select a file.
     *
     * @param defaultOpenLocation staring directory to open the dialog box
     * @param stage               the stage that is the dialog's owner
     * @param title               the tile of the file selector window
     * @return the file object of the selected file or null if not
     */
    public static File promptUserForFile(String defaultOpenLocation, Stage stage, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(defaultOpenLocation));
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Opens a dialog for the user to select a folder.
     *
     * @param defaultOpenLocation staring directory to open the dialog box
     * @param stage               the stage that is the dialog's owner
     * @param title               the tile of the folder selector window
     * @return the file object of the selected folder or null if not
     */
    public static File promptUserForFolder(String defaultOpenLocation, Stage stage, String title) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(defaultOpenLocation));
        return fileChooser.showDialog(stage);
    }

    public static void fatalIfDebug() {
        if (!DEBUG)
            return;
        throw new RuntimeException("BIG ERROR!!!");
    }

    public static void exception(Exception e) {
        e.printStackTrace();
    }
}
