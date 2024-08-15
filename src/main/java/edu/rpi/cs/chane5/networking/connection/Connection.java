package edu.rpi.cs.chane5.networking.connection;

import edu.rpi.cs.chane5.networking.commands.Command;

/**
 * Base abstract class for containing basic methods required for a Connection subclass.
 *
 * @version {@value VERSION}
 */
public abstract class Connection {
    /**
     * Default port the server listens on.
     */
    public static final int DEFAULT_PORT = 4825;
    /**
     * Version of this package.
     */
    static final String VERSION = "1.0";
    /**
     * The currently connected port number.
     */
    protected final int port;

    /**
     * Creates using default port.
     */
    protected Connection() {
        this(DEFAULT_PORT);
    }

    /**
     * Creates using a specified port.
     *
     * @param port the port to connect on
     */
    public Connection(int port) {
        this.port = port;
    }

    /**
     * Connects to the peer
     *
     * @throws Exception if connection fails
     */
    public abstract void connect() throws Exception;

    /**
     * Sends a message to the peer.
     *
     * @param message command to send
     */
    public abstract void send(Command message);

    /**
     * When a message is received.
     *
     * @return a parsed command
     */
    public abstract Command receive();

    /**
     * Gets the port on.
     *
     * @return the port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Closes the connection.
     *
     * @throws Exception if there is an error with closing
     */
    public abstract void close() throws Exception;
}
