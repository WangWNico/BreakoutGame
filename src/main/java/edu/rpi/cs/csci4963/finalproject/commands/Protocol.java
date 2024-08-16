package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.csci4963.finalproject.BreakoutApplication;
import edu.rpi.cs.csci4963.finalproject.BreakoutController;
import edu.rpi.cs.csci4963.finalproject.StartMenuController;
import javafx.application.Platform;

import static edu.rpi.cs.chane5.Utils.debug;

/**
 * The Protocol class provides static methods to handle different types of commands.
 * It is a utility class and should not be instantiated.
 */
public class Protocol {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Protocol() {
        // util class, no need to instantiate
    }

    /**
     * Handles the given command by determining its type and executing the appropriate action.
     *
     * @param command the command to handle
     */
    public static void handle(Command command) {
        debug("called");
        if (command instanceof HandshakeCommand) {
            debug("received Handshake Command");
            BreakoutApplication.get().getConnection().send(new StartGameCommand());
            BreakoutApplication.get().closeLoadingScreen();
        } else if (command instanceof StartGameCommand) {
            StartMenuController.getStartMenuController().remoteStartGame();
            BreakoutApplication.get().closeLoadingScreen();
            Platform.runLater(() -> BreakoutController.get().hideRestartButton());
        } else if (command instanceof EndGameCommand) {
            BreakoutController.get().multiplayerEndGame();
        } else if (command instanceof WinGameCommand) {
            BreakoutController.get().multiplayerWinGame();
        }
    }
}