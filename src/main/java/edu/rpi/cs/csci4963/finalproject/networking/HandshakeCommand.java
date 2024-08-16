package edu.rpi.cs.csci4963.finalproject.networking;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

public class HandshakeCommand extends Command {
    private String version;
    @Override
    public Command applyParser(CommandParser parser) {
        HandshakeCommand cmd = new HandshakeCommand();
        cmd.version = "1.0";
        return cmd;
    }

    @Override
    public String getParsedCommand() {
        return version;
    }

    @Override
    public Command newInstance() {
        return new HandshakeCommand();
    }
}
