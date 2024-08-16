package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * The EndGameCommand class represents a command to end the game.
 * It extends the Command class and provides implementations for the required methods.
 */
public class EndGameCommand extends Command {

    /**
     * Constructs a new EndGameCommand with the command name "endgame".
     */
    public EndGameCommand() {
        super("endgame");
    }

    /**
     * Applies the given CommandParser to this command.
     *
     * @param parser the CommandParser to apply
     * @return this EndGameCommand instance
     */
    @Override
    public Command applyParser(CommandParser parser) {
        return this;
    }

    /**
     * Returns the parsed command as a string.
     *
     * @return the name of the command
     */
    @Override
    public String getParsedCommand() {
        return getName();
    }

    /**
     * Creates a new instance of the EndGameCommand.
     *
     * @return a new EndGameCommand instance
     */
    @Override
    public Command newInstance() {
        return new EndGameCommand();
    }
}