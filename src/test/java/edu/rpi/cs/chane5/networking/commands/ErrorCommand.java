package edu.rpi.cs.chane5.networking.commands;

public class ErrorCommand extends Command {
    public ErrorCommand() {
        super("error");
    }

    @Override
    public ErrorCommand applyParser(CommandParser parser) {
        return this;
    }

    @Override
    public String getParsedCommand() {
        return getName();
    }

    @Override
    public Command newInstance() {
        return new ErrorCommand();
    }

}
