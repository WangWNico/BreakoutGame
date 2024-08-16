package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.ArgumentFormat;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * The HandshakeCommand class represents a command used for initiating a handshake.
 * It extends the Command class and includes a version argument.
 */
public class HandshakeCommand extends Command {
    private String version;

    /**
     * Constructs a new HandshakeCommand with the default version set to "1.0".
     */
    public HandshakeCommand() {
        super("handshake");
        addArgument(new ArgumentBase("version", ArgumentFormat.STRING));
        this.version = "1.0";
    }

    /**
     * Applies the given CommandParser to create a new HandshakeCommand instance.
     *
     * @param parser the CommandParser to apply
     * @return a new HandshakeCommand instance with the version set to "1.0"
     */
    @Override
    public Command applyParser(CommandParser parser) {
        HandshakeCommand cmd = new HandshakeCommand();
        cmd.version = "1.0";
        return cmd;
    }

    /**
     * Returns the parsed command as a string.
     *
     * @return the parsed command string in the format "handshake|version"
     */
    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + version;
    }

    /**
     * Creates a new instance of HandshakeCommand.
     *
     * @return a new HandshakeCommand instance
     */
    @Override
    public Command newInstance() {
        return new HandshakeCommand();
    }
}