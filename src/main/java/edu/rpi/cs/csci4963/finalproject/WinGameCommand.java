package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * The WinGameCommand class represents a command to indicate that a player has won the game.
 * It extends the Command class and provides implementations for the required methods.
 */
public class WinGameCommand extends Command {

    /**
     * Constructs a new WinGameCommand with the command name set to "win".
     */
    public WinGameCommand() {
        super("win");
    }

    /**
     * Applies the given CommandParser to this command.
     *
     * @param parser the CommandParser to apply
     * @return this WinGameCommand instance
     */
    @Override
    public Command applyParser(CommandParser parser) {
        return this;
    }

    /**
     * Returns the parsed command as a string.
     *
     * @return the parsed command string, which is the command name
     */
    @Override
    public String getParsedCommand() {
        return getName();
    }

    /**
     * Creates a new instance of WinGameCommand.
     *
     * @return a new WinGameCommand instance
     */
    @Override
    public Command newInstance() {
        return new WinGameCommand();
    }
}