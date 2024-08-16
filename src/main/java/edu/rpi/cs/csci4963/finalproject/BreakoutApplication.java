package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.Utils;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.connection.Client;
import edu.rpi.cs.chane5.networking.connection.Connection;
import edu.rpi.cs.chane5.networking.connection.Server;
import edu.rpi.cs.csci4963.finalproject.networking.HandshakeCommand;
import edu.rpi.cs.csci4963.finalproject.networking.Protocol;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import static edu.rpi.cs.chane5.Utils.*;

public class BreakoutApplication extends Application {
    private static boolean isJavaFxRunning = false;
    private Thread threadIncoming;
    // the socket connection
    private Connection connection;



    // variable to open the start screen on startup or not
    private static boolean openLauncher = false;
    // queue processing thread
    private static Thread threadQueue;
    // connection thread, aka main(String[]) thread
    private static Thread threadConnection;
    private static LinkedBlockingQueue<Runnable> connectionQueue = new LinkedBlockingQueue<>();
    private final int LOSS_THRESHOLD = 25;
    // JavaFX Thread
    private Thread threadJavaFX;




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
//                            getJavaFxApp().setUpdateMessage("ERROR: Peer lost connection!\nRestart Game");
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
            // todo gracefully handle when connections disconnect AKA alert user
        }, "NetworkingIncoming-" + new Date().getTime());
    }

    // alerts of closing connecting then terminates
    private void closeExisting() throws Exception {
        debug("called");
        if (connection == null)
            return;
        // todo send exit/close command
        connection.close();
        if (threadConnection != null)
            threadConnection.interrupt();
    }

    /**
     * Starts a server listening on the specified port.
     *
     * @param port the port to listen on
     * @throws Exception an I/O error occurred or security error
     */
    public void initServer(int port) throws Exception {
        // NOTE NOT RUNNING ON SAME THREAD AS MAIN
        debug("called");


//        javaFxApp.setWindowTitle("Battleship - Server Awaiting Connection..");
        if (Thread.currentThread() != threadConnection) {
            connectionQueue.put(() -> {
                try {
//                    javaFxApp.setUpdateMessage("Starting server on port " + port);
                    initServer(port);
                    String msg = "Hosting on localhost:" + port;
                    debug(msg);
//                    javaFxApp.setUpdateMessage(msg);
                } catch (Exception e) {
                    errorTrace("starting server on port " + port);
                    alertError("Starting Server", "Error starting the server on port " + port + '\n' + e.getMessage());
                    e.printStackTrace();
//                    javaFxApp.setUpdateMessage("");
                }
            });
            return;
        }

        closeExisting();
        initThreadIncoming();
        connection = new Server(port);
        connection.connect();
        if (!threadIncoming.isAlive())
            threadIncoming.start();
//        connection.send(new HandshakeCommand().setState(HandshakeCommand.State.INIT));
//        javaFxApp.setWindowTitle("Battleship - Server");
    }

    /**
     * Starts a client at the given address and port.
     *
     * @param address the IPv4 address of the sever
     * @param port    the port number of the server
     * @throws Exception if there is an I/O error
     */
    public void initClient(String address, int port) throws Exception {
        // NOTE NOT RUNNING ON SAME THREAD AS MAIN
        debug("called");

//        javaFxApp.setWindowTitle("Battleship - Client Awaiting Connection...");
//        javaFxApp.setUpdateMessage("Awaiting server to ready...");
        if (Thread.currentThread() != threadConnection) {
            connectionQueue.put(() -> {
                try {
                    initClient(address, port);
                } catch (Exception e) {
                    errorTrace("starting server on port " + port);
                    alertError("Starting Client", "There was an error in connecting to " + address + ":" + port + "\n" + e.getMessage());
                    e.printStackTrace();
//                    javaFxApp.setUpdateMessage("");
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
     * @return the Connection instance.
     * @see Connection
     */
    public Connection getConnection() {
        debug("called");
        return connection;
    }
}