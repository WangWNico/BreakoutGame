package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * The RestartGameCommand class represents a command to restart the game.
 * It extends the Command class and provides implementations for the required methods.
 */
public class RestartGameCommand extends Command {

    /**
     * Constructs a new RestartGameCommand with the command name set to "restart".
     */
    public RestartGameCommand() {
        super("restart");
    }

    /**
     * Applies the given CommandParser to this command.
     *
     * @param parser the CommandParser to apply
     * @return this RestartGameCommand instance
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
     * Creates a new instance of RestartGameCommand.
     *
     * @return a new RestartGameCommand instance
     */
    @Override
    public Command newInstance() {
        return new RestartGameCommand();
    }
}