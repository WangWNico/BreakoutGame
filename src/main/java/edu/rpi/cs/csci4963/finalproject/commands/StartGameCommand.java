package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * The StartGameCommand class represents a command to start the game.
 * It extends the Command class and provides implementations for the required methods.
 */
public class StartGameCommand extends Command {

    /**
     * Constructs a new StartGameCommand with the command name set to "start".
     */
    public StartGameCommand() {
        super("start");
    }

    /**
     * Applies the given CommandParser to this command.
     *
     * @param parser the CommandParser to apply
     * @return this StartGameCommand instance
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
     * Creates a new instance of StartGameCommand.
     *
     * @return a new StartGameCommand instance
     */
    @Override
    public Command newInstance() {
        return new StartGameCommand();
    }
}