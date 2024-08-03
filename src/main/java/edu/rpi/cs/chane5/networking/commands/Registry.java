package edu.rpi.cs.chane5.networking.commands;

import edu.rpi.cs.chane5.networking.connection.Connection;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry containing all registered example_commands in the program. The example_commands need to be registered in order of {@link Connection#receive()}
 * to process the data into example_commands.<br>
 * Acts as a singleton with a mapping from the command name to the command object.
 *
 * @version {@value Command#VERSION}
 */
public class Registry {
    private static Registry registry = null;
    private final Map<String, Command> registryMap;

    /**
     * Private default constructor for singleton.
     */
    private Registry() {
        registryMap = new HashMap<>();
    }

    /**
     * Gets the singleton Registry instance
     *
     * @return the registry singleton instance
     */
    public static Registry get() {
        if (registry == null)
            registry = new Registry();
        return registry;
    }

    /**
     * Gets the command object associated with the name.
     *
     * @param name name of the command
     * @return the command object, otherwise returns null
     */
    public Command get(String name) {
        return registryMap.get(name);
    }

    /**
     * Adds a command to the registry. If an existing command with that name exists, replaces it.
     *
     * @param command command to register
     * @return this registry
     */
    public Registry register(Command command) {
        registryMap.put(command.getName(), command);
        return this;
    }
}
