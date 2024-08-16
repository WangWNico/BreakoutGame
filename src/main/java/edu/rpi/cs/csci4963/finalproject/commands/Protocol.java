package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.csci4963.finalproject.BreakoutApplication;
import edu.rpi.cs.csci4963.finalproject.BreakoutController;
import edu.rpi.cs.csci4963.finalproject.StartMenuController;
import edu.rpi.cs.csci4963.finalproject.WinGameCommand;

import static edu.rpi.cs.chane5.Utils.debug;

public class Protocol {
    private Protocol() {
        // util class, no need to instantiate
    }

    public static void handle(Command command) {
        debug("called");
        if (command instanceof HandshakeCommand) {
            debug("received Handshake Command");
            BreakoutApplication.get().getConnection().send(new StartGameCommand());
            BreakoutApplication.get().closeLoadingScreen();
        } else if (command instanceof StartGameCommand) {
            StartMenuController.getStartMenuController().remoteStartGame();
            BreakoutApplication.get().closeLoadingScreen();
        } else if (command instanceof EndGameCommand) {
            BreakoutController.get().multiplayerEndGame();
        } else if (command instanceof WinGameCommand) {
            BreakoutController.get().multiplayerWinGame();
        }
    }
}
