package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.ArgumentFormat;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

public class HandshakeCommand extends Command {
    private String version;

    public HandshakeCommand() {
        super("handshake");
        addArgument(new ArgumentBase("version", ArgumentFormat.STRING));
        this.version = "1.0";
    }

    @Override
    public Command applyParser(CommandParser parser) {
        HandshakeCommand cmd = new HandshakeCommand();
        cmd.version = "1.0";
        return cmd;
    }

    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + version;
    }

    @Override
    public Command newInstance() {
        return new HandshakeCommand();
    }
}
