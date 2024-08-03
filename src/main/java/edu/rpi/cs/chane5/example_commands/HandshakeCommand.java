package edu.rpi.cs.chane5.example_commands;

import edu.rpi.cs.chane5.networking.commands.ArgumentFormat;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;
import edu.rpi.cs.csci4963.finalproject.HelloApplication;

/**
 * Command for handshaking at the start of the game. <br />
 * Schema: <code>handshake state version</code>
 * <br>
 * Expected handshake:
 * <ol>
 *     <li>server sends >> <code>State.INIT</code> <code><i>version</i></code></li>
 *     <li>client sends >> <code>State.INIT</code> <code><i>version</i></code></li> 
 *     <li>At this point the versions should be verified the same.</li>
 *     <li>Server assigns the client a player number >> <code>State.PLAYER1</code> or <code>State.PLAYER2</code> </li>
 *     <li>client acknowledges by sending back what their player state is</li>
 * </ol>
 *
 * @version {@value HelloApplication#VERSION}
 */
public class HandshakeCommand extends Command {
    private State state = State.INIT;
    private String version = HelloApplication.VERSION;

    /**
     * Creates a new {@link HandshakeCommand} command.
     */
    public HandshakeCommand() {
        super("handshake");
        addArgument(new ArgumentBase("state", ArgumentFormat.CHAR));
        addArgument(new ArgumentBase("version", ArgumentFormat.STRING));
    }

    /**
     * Gets the state of the handshake.
     *
     * @return the state of the handshake.
     * @see State
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the handshake from {@link State}.
     *
     * @param state the state of the handshake
     * @return this handshake command
     */
    public HandshakeCommand setState(State state) {
        this.state = state;
        return this;
    }

    @Override
    public HandshakeCommand applyParser(CommandParser parser) {
        state = State.valueOf(parser.getChar("state"));
        version = parser.getString("version");
        return this;
    }

    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + state.value + DELIMITER + HelloApplication.VERSION;
    }

    @Override
    public Command newInstance() {
        return new HandshakeCommand();
    }

    /**
     * Gets the version of the handshake. Is expected to be the same value as {@link HelloApplication#VERSION}.
     *
     * @return the version of the handshake
     */
    public String getVersion() {
        return version;
    }

    /**
     * States the handshaking command can be in. <br>
     * See {@link HandshakeCommand} for the expected protocol.
     */
    public enum State {
        /**
         * Initialization State
         */
        INIT('X'),
        /**
         * Player 1 State
         */
        PLAYER1('1'),
        /**
         * Player 2 State
         */
        PLAYER2('2');
        /**
         * The value of the current state.
         */
        public final char value;

        /**
         * Creates a state with value.
         *
         * @param value the value of the state
         */
        State(char value) {
            this.value = value;
        }

        /**
         * Attempts to parse the char into a state. If no state is found returns null.
         *
         * @param c the char to parse
         * @return If found, the state associated with the char. Otherwise, returns null.
         */
        public static State valueOf(char c) {
            for (State state : State.values()) {
                if (state.value == c)
                    return state;
            }
            return null;
        }
    }
}
