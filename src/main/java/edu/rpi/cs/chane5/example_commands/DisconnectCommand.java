package edu.rpi.cs.chane5.example_commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * Sends a disconnect message to the peer for exiting the connection gracefully.
 */
public class DisconnectCommand extends Command {
    /**
     * Creates a new instance of disconnect.
     */
    public DisconnectCommand() {
        super("disconnect");
    }

    @Override
    public DisconnectCommand applyParser(CommandParser parser) {
        return newInstance();
    }

    @Override
    public String getParsedCommand() {
        return getName();
    }

    @Override
    public DisconnectCommand newInstance() {
        return new DisconnectCommand();
    }
}
