package edu.rpi.cs.chane5.example_commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * Ready command for signaling to peer all ships are placed.
 */
public class ReadyCommand extends Command {

    /**
     * Creates a new instance of the ready command.
     */
    public ReadyCommand() {
        super("ready");
    }

    @Override
    public Command applyParser(CommandParser parser) {
        return newInstance();
    }

    @Override
    public String getParsedCommand() {
        return getName();
    }

    @Override
    public Command newInstance() {
        return new ReadyCommand();
    }
}
