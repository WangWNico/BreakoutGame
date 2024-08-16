package edu.rpi.cs.csci4963.finalproject.commands;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

public class EndGameCommand extends Command {

    public EndGameCommand() {
        super("endgame");
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
        return new EndGameCommand();
    }
}
