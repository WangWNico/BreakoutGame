package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.Registry;
import edu.rpi.cs.chane5.networking.connection.Client;
import edu.rpi.cs.chane5.networking.connection.Connection;
import edu.rpi.cs.chane5.networking.connection.Server;
import edu.rpi.cs.csci4963.finalproject.commands.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static edu.rpi.cs.chane5.Utils.*;

/**
 * The BreakoutApplication class is the main entry point for the Breakout game application.
 * It handles the initialization of the JavaFX application, networking connections, and command processing.
 */
public class BreakoutApplication extends Application {
    private static boolean isJavaFxRunning = false;
    // variable to open the start screen on startup or not
    private static boolean openLauncher = false;
    // queue processing thread
    private static Thread threadQueue;
    // connection thread, aka main(String[]) thread
    private static Thread threadConnection;
    private static LinkedBlockingQueue<Runnable> connectionQueue = new LinkedBlockingQueue<>();
    // JavaFX Thread
    private static Thread threadJavaFX;
    private static BreakoutApplication breakoutApplication;
    private final int LOSS_THRESHOLD = 25;
    private Thread threadIncoming;
    // the socket connection
    private Connection connection;
    private Stage loadingScreen;

    /**
     * Constructs a new BreakoutApplication instance and sets the static reference.
     */
    public BreakoutApplication() {
        breakoutApplication = this;
    }

    /**
     * The main entry point for the application.
     * It processes command-line arguments, registers commands, and starts the necessary threads.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("--debug".equals(args[i]))
                DEBUG_MODE = true;
        }

        // register commands
        Registry registry = Registry.get();
        registry.register(new HandshakeCommand());
        registry.register(new StartGameCommand());
        registry.register(new EndGameCommand());
        registry.register(new WinGameCommand());

        // connection thread
        threadQueue = new Thread(() -> {
            try {
                while (true) {
                    Runnable runnable = connectionQueue.poll(3, TimeUnit.SECONDS);
                    if (runnable != null) {
                        if (threadConnection != null)
                            threadConnection.interrupt();
                        threadConnection = new Thread(runnable);
                        threadConnection.start();
                    }
                }
            } catch (InterruptedException e) {
                errorTrace("connectionQueue thread was interrupted");
            }
        }, "connectionQueue");
        threadQueue.start();

        // init javafx on separate thread
        threadJavaFX = new Thread(Application::launch, "JavaFX Main");
        threadJavaFX.start();
    }

    /**
     * Checks if the JavaFX application is running.
     *
     * @return true if JavaFX is running, false otherwise
     */
    public static boolean isJavaFXRunning() {
        return isJavaFxRunning;
    }

    /**
     * Gets the singleton instance of BreakoutApplication.
     *
     * @return the BreakoutApplication instance
     */
    public static BreakoutApplication get() {
        return breakoutApplication;
    }

    /**
     * Creates a new loading screen with the specified message and shows it on the screen.
     * Must be run on the JavaFX Thread.
     *
     * @param msg the text to display in the window
     * @return a loading screen stage
     */
    public Stage startLoadingScreen(String msg) {
        debug("loading screen");

        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Loading...");
        stage.show();

        Label label = new Label();
        label.setText(msg);
        label.setFont(new Font(18));
        label.textAlignmentProperty().setValue(TextAlignment.CENTER);

        VBox containerVbox = new VBox(10);
        containerVbox.setAlignment(Pos.CENTER);
        containerVbox.getChildren().add(label);

        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.getChildren().add(containerVbox);

        Scene scene = new Scene(container, 300, 300);
        stage.setResizable(false);
        stage.setScene(scene);
        return stage;
    }

    /**
     * Starts the JavaFX application and initializes the main stage.
     *
     * @param stage the primary stage for this application
     * @throws Exception if an error occurs during loading
     */
    @Override
    public void start(Stage stage) throws Exception {
        isJavaFxRunning = true;
        FXMLLoader fxmlLoader = new FXMLLoader(BreakoutApplication.class.getResource("start-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
        scene.getStylesheets().add(BreakoutApplication.class.getResource("styles.css").toExternalForm()); // Load CSS
        stage.setTitle("Breakout Game");
        stage.setScene(scene);
        stage.show();
        ((StartMenuController) fxmlLoader.getController()).setApplicationInstance(this);
    }

    /**
     * Initializes the thread for processing incoming network connections.
     */
    private void initThreadIncoming() {
        // incoming thread for processing blocking connections
        threadIncoming = new Thread(() -> {
            int loss = 0;
            while (true) {
                Command command = connection.receive();
                if (command == null) {
                    debug("packet loss!!!");
                    loss++;
                    if (LOSS_THRESHOLD < loss) {
                        debug("Loss enough packets to assume connection severed!");
                        Platform.runLater(() -> {
                            debug("Multiplayer opponent lost connection! Restart program!");
                            alertError("Multiplayer", "Multiplayer opponent lost connection!\nRestart program!", (buttonType) -> {
                                // todo open start screen again
                            });
                        });
                        break;
                    }
                    continue;
                }
                loss = 0;
                Protocol.handle(command);
            }
            debug("END of #initThreadIncoming() > Thread-NetworkingIncoming");
            // NOTE: is interruptible
        }, "NetworkingIncoming-" + new Date().getTime());
    }

    /**
     * Closes the existing connection and interrupts the connection thread.
     *
     * @throws Exception if an error occurs while closing the connection
     */
    private void closeExisting() throws Exception {
        debug("called");
        if (connection == null)
            return;
        connection.close();
        if (threadConnection != null)
            threadConnection.interrupt();
    }

    /**
     * Starts a server listening on the specified port.
     *
     * @param port the port to listen on
     * @throws Exception if an I/O error occurs or a security error
     */
    public void initServer(int port) throws Exception {
        // NOTE NOT RUNNING ON SAME THREAD AS MAIN
        debug("called");

        if (Thread.currentThread() != threadConnection) {
            connectionQueue.put(() -> {
                try {
                    initServer(port);
                    debug("Hosting on localhost:" + port);
                } catch (Exception e) {
                    errorTrace("starting server on port " + port);
                    alertError("Starting Server", "Error starting the server on port " + port + '\n' + e.getMessage());
                    e.printStackTrace();
                }
            });
            return;
        }

        AtomicReference<Stage> stage = new AtomicReference<>();
        Platform.runLater(() -> loadingScreen = startLoadingScreen("Starting Server...\nWaiting for connection..."));
        closeExisting();
        initThreadIncoming();
        connection = new Server(port);
        connection.connect();
        if (!threadIncoming.isAlive())
            threadIncoming.start();

        if (stage.get() != null)
            Platform.runLater(() -> stage.get().hide());
        connection.send(new HandshakeCommand());
    }

    /**
     * Starts a client at the given address and port.
     *
     * @param address the IPv4 address of the server
     * @param port    the port number of the server
     * @throws Exception if there is an I/O error
     */
    public void initClient(String address, int port) throws Exception {
        // NOTE NOT RUNNING ON SAME THREAD AS MAIN
        debug("called");

        if (Thread.currentThread() != threadConnection) {
            connectionQueue.put(() -> {
                try {
                    initClient(address, port);
                } catch (Exception e) {
                    errorTrace("starting server on port " + port);
                    alertError("Starting Client", "There was an error in connecting to " + address + ":" + port + "\n" + e.getMessage());
                    e.printStackTrace();
                }
            });
            return;
        }

        closeExisting();
        initThreadIncoming();
        connection = new Client(address, port);
        connection.connect();
        if (!threadIncoming.isAlive())
            threadIncoming.start();

        connection.send(new HandshakeCommand());
    }

    /**
     * Gets the Connection instance.
     *
     * @return the Connection instance
     * @see Connection
     */
    public Connection getConnection() {
        debug("called");
        return connection;
    }

    /**
     * Stops the JavaFX application and exits the program.
     */
    @Override
    public void stop() {
        debug("exiting...");
        System.exit(0);
    }

    /**
     * Closes the loading screen if it is currently displayed.
     */
    public void closeLoadingScreen() {
        if (loadingScreen != null)
            Platform.runLater(loadingScreen::hide);
    }
}