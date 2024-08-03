package edu.rpi.cs.chane5.networking.commands;

import edu.rpi.cs.chane5.Utils;
import edu.rpi.cs.chane5.networking.connection.Client;
import edu.rpi.cs.chane5.networking.connection.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static edu.rpi.cs.chane5.Utils.fatalIfDebug;

/**
 * Class used for parsing incoming data from {@link Client#receive()} and {@link Server#receive()} into a
 * {@link Command} argument specification. Once parsed, {@link Command#applyParser(CommandParser)} can be called to
 * populate the example_commands arguments for further use.
 *
 * @version {@value Command#VERSION}
 */
public class CommandParser {
    private final Map<String, String> argMapString;
    private final Map<String, Integer> argMapInt;
    private final Map<String, Double> argMapDouble;
    private final Map<String, Character> argMapChar;
    // true indicates processed through entire command, false indicates parse() went wrong
    private boolean successful = false;
    // true indicates an error occurred somewhere, therefore stay failed
    private boolean forceUnsuccessful = false;
    private final Map<String, Boolean> argMapBoolean;

    /**
     * Use the {@link #parse(Command, String)} method to instantiate this class.
     */
    private CommandParser() {
        argMapString = new HashMap<>();
        argMapInt = new HashMap<>();
        argMapDouble = new HashMap<>();
        argMapChar = new HashMap<>();
        argMapBoolean = new HashMap<>();
    }

    /**
     * Attempts to parse the whitespace delimited arguments according to the command's arguments.
     *
     * @param command   the command to parse against
     * @param arguments the arguments string whitespace delimited to parse
     * @return an instance of CommandParser with the arguments filled in, null if an error occurred
     */
    public static CommandParser parse(Command command, String arguments) {
        CommandParser handler = new CommandParser();

        // no argument short case
        final int EXPECTED_ARGUMENT_COUNT = command.getArgumentCount();
        if (EXPECTED_ARGUMENT_COUNT == 0)
            return handler;

        // argument lengths don't match
        final String[] args = arguments.trim().split(String.valueOf(Command.DELIMITER));
        if (EXPECTED_ARGUMENT_COUNT != args.length) {
            handler.forceUnsuccessful = true;
            fatalIfDebug();
            return handler;
        }

        List<Command.ArgumentBase> cmdArgs = command.getArguments();
        for (int i = 0; i < args.length; i++) {
            Command.ArgumentBase cmdArg = cmdArgs.get(i);
            switch (cmdArg.argumentFormat()) {
                case STRING:
                    handler.parseArgString(cmdArg.argumentName(), args[i]);
                    break;
                case INT:
                    handler.parseArgInt(cmdArg.argumentName(), args[i]);
                    break;
                case DOUBLE:
                    handler.parseArgDouble(cmdArg.argumentName(), args[i]);
                    break;
                case CHAR:
                    handler.parseArgChar(cmdArg.argumentName(), args[i]);
                    break;
                case BOOL:
                    handler.parseArgBoole(cmdArg.argumentName(), args[i]);
                    break;
                default:
                    handler.forceUnsuccessful = true;
                    fatalIfDebug();
                    break;
            }
        }

        return handler.successful();
    }

    private void parseArgBoole(String name, String arg) {
        try {
            boolean parsed = Boolean.parseBoolean(arg);
            argMapBoolean.put(name, parsed);
        } catch (Exception e) {
            Utils.exception(e);
            forceUnsuccessful = true;
        }
    }

    private void parseArgChar(String name, String arg) {
        if (arg.isEmpty()) {
            fatalIfDebug();
            forceUnsuccessful = true;
        }
        argMapChar.put(name, arg.charAt(0));
    }

    private void parseArgDouble(String name, String arg) {
        try {
            double parsed = Double.parseDouble(arg);
            argMapDouble.put(name, parsed);
        } catch (Exception e) {
            Utils.exception(e);
            forceUnsuccessful = true;
        }
    }

    private void parseArgString(String name, String arg) {
        argMapString.put(name, arg);
    }

    private void parseArgInt(String name, String arg) {
        try {
            int parsed = Integer.parseInt(arg);
            argMapInt.put(name, parsed);
        } catch (Exception e) {
            Utils.exception(e);
            forceUnsuccessful = true;
        }
    }

    private CommandParser successful() {
        this.successful = true;
        return this;
    }

    /**
     * Indicates if the parsing operation was successful against the command.
     *
     * @return true is successful, false if there was an error
     */
    public boolean isSuccessful() {
        return !forceUnsuccessful && successful;
    }

    /**
     * Gets the argument of string type with name.
     *
     * @param name name of argument
     * @return the entryValue of the argument
     */
    public String getString(String name) {
        Objects.requireNonNull(name);
        return argMapString.get(name);
    }

    /**
     * Gets the argument of int type with name.
     *
     * @param name name of argument
     * @return the entryValue of the argument
     */
    public int getInt(String name) {
        Objects.requireNonNull(name);
        return argMapInt.get(name);
    }

    /**
     * Gets the argument of double type with name.
     *
     * @param name name of argument
     * @return the entryValue of the argument
     */
    public Double getDouble(String name) {
        Objects.requireNonNull(name);
        return argMapDouble.get(name);
    }

    /**
     * Gets the argument of char type with name.
     *
     * @param name name of argument
     * @return the entryValue of the argument
     */
    public char getChar(String name) {
        Objects.requireNonNull(name);
        return argMapChar.get(name);
    }

    /**
     * Gets the argument of boolean type with name.
     *
     * @param name name of argument
     * @return the entryValue of the argument
     */
    public boolean getBoolean(String name) {
        Objects.requireNonNull(name);
        return argMapBoolean.get(name);
    }
}
