package edu.rpi.cs.csci4963.finalproject;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

public class WinGameCommand extends Command {
    public WinGameCommand() {
        super("win");
    }

    @Override
    public Command applyParser(CommandParser parser) {
        return this;
    }

    @Override
    public String getParsedCommand() {
        return getName();
    }

    @Override
    public Command newInstance() {
        return new WinGameCommand();
    }
}
