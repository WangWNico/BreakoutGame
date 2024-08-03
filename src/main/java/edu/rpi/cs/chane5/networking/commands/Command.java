package edu.rpi.cs.chane5.networking.commands;

import edu.rpi.cs.chane5.networking.connection.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Command class base used to create example_commands for the network protocol between {@link edu.rpi.cs.chane5.networking.connection.Client}
 * and {@link edu.rpi.cs.chane5.networking.connection.Server}. The parsed command uses a <code>{@value DELIMITER}</code> as a delimiter after
 * the command name and between each argument. The parsed string is in the format <code>name{@value DELIMITER}arg{@value DELIMITER}arg2</code> with as many
 * arguments as needed. <br>
 * All example_commands need to be registered with {@link Registry#register(Command)} in order for the protocol in {@link Connection#receive()}
 * to be able to process that command.
 *
 * @version {@value VERSION}
 */
public abstract class Command {
    /**
     * Deliminator used between arguments.
     */
    public static final char DELIMITER = '␁';
    /**
     * Version of this package.
     */
    static final String VERSION = "1.0";
    private final String name;
    private final List<ArgumentBase> argumentBases = new ArrayList<>();

    /**
     * Creates a Command object. Intended to be constructed from the CommandBuilder.
     *
     * @param name the name of the command
     */
    protected Command(String name) {
        this.name = name;
    }

    /**
     * Not intended to construct with default constructor.
     *
     * @see #Command(String)
     */
    public Command() {
        this("cmd-" + new Date().getTime());
    }

    /**
     * Adds a new argumentBase to the command.
     *
     * @param argumentBase the argumentBase name and type
     * @return this command instance
     */
    protected Command addArgument(ArgumentBase argumentBase) {
        argumentBases.add(argumentBase);
        return this;
    }

    /**
     * Gets an immutable list of all the argumentBases.
     *
     * @return the argumentBases list, is immutable
     */
    protected List<ArgumentBase> getArguments() {
        return Collections.unmodifiableList(argumentBases);
    }

    /**
     * Handle applying the parser's arguments into the field methods.
     *
     * @param parser the parser with all the argument values stored
     * @return the instance of this command
     */
    public abstract Command applyParser(CommandParser parser);

    /**
     * Turns the various arguments into a command string.
     *
     * @return the parsed version of the command with name followed by the arguments separated with delimiter <code>{@value DELIMITER}</code>
     */
    public abstract String getParsedCommand();

    /**
     * Gets the name of the command
     *
     * @return the name of the command
     */
    public final String getName() {
        return name;
    }

    /**
     * Gets a string representation of the command. In the form [NAME] [ARGUMENTS...]
     *
     * @return a string representation of the command
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // name
        builder.append(name).append(DELIMITER);
        // all argumentBases
        argumentBases.forEach(argumentBase -> builder.append(argumentBase.argumentName).append(DELIMITER));
        // removes trailing '|'
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    /**
     * Gets the number of argumentBases in this command.
     *
     * @return number of argumentBases in this command.
     */
    public int getArgumentCount() {
        return argumentBases.size();
    }

    /**
     * Returns a new instance of this command type. Invokes the recommended constructor with appropriate values if applicable.
     * Note, this can sometimes return an identical copy, however that is not guaranteed.
     *
     * @return a new instance of this command
     */
    public abstract Command newInstance();

    /**
     * Stores the argument with its name and type.
     *
     * @param argumentName   name of the argument
     * @param argumentFormat type of the argument
     */
    public record ArgumentBase(String argumentName, ArgumentFormat argumentFormat) {
    }
}
