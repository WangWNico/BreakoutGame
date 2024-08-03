package edu.rpi.cs.chane5.networking.connection;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;
import edu.rpi.cs.chane5.networking.commands.Registry;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static edu.rpi.cs.chane5.Utils.debug;
import static edu.rpi.cs.chane5.Utils.errorTrace;

/**
 * Creates a Server for listening to data from a client. Currently only supports one client. The protocol is determined
 * by the various example_commands registered in the {@link Registry}.
 *
 * @version {@value Connection#VERSION}
 */
public class Server extends Connection {
    private final ServerSocket serverSocket;
    private final Registry registry;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Scanner reader;
    private PrintWriter writer;

    /**
     * Creates a new Server instance using the specified port.
     *
     * @param port the port the server will listen on
     * @throws Exception as per the exceptions specified in {@link ServerSocket#ServerSocket(int)}
     */
    public Server(int port) throws Exception {
        super(port);
        this.serverSocket = new ServerSocket(this.port);
        this.registry = Registry.get();
    }

    /**
     * Creates a new Server instance using the <code>DEFAULT_PORT</code>.
     *
     * @throws Exception as per the exceptions specified in {@link ServerSocket#ServerSocket(int)}
     */
    public Server() throws Exception {
        this(DEFAULT_PORT);
    }

    /**
     * Establishes the connection and opens all appropriate streams.
     *
     * @throws Exception An error occurred creating the connection or stream error.
     */
    @Override
    public void connect() throws Exception {
        this.socket = this.serverSocket.accept();
        this.inputStream = this.socket.getInputStream();
        this.outputStream = this.socket.getOutputStream();
        this.reader = new Scanner(this.inputStream);
        this.writer = new PrintWriter(new OutputStreamWriter(this.outputStream, StandardCharsets.UTF_8), true);
    }

    /**
     * Sends a command data through the socket.
     *
     * @param message command to send
     */
    @Override
    public void send(Command message) {
        Objects.requireNonNull(message);
        if (writer == null)
            return;
        writer.println(message.getParsedCommand());
        writer.flush();
        debug(message.getParsedCommand());
    }

    /**
     * Parses incoming socket data into a command object.
     *
     * @return the command object of the data, otherwise null for nothing applicable
     */
    @Override
    public Command receive() {
        String input;
        try {
            input = reader.nextLine();
            debug(input);
        } catch (Exception e) {
            errorTrace("failed reading");
            return null;
        }

        // substring off by first space delimiter
        int delimiter = input.indexOf(Command.DELIMITER);
        Command cmd = registry.get(input.substring(0, delimiter == -1 ? input.length() : delimiter));

        // null not found
        if (cmd == null)
            return null;

        // only command header, no args
        if (delimiter == -1)
            return cmd;

        // parse args
        CommandParser parser = CommandParser.parse(cmd, input.substring(delimiter + 1));
        if (!parser.isSuccessful())
            return null;

        try {
            // make copy of cmd and apply args
            return cmd.newInstance().applyParser(parser);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Closes the server connection.
     *
     * @throws IOException when there is an I/O error
     */
    @Override
    public void close() throws Exception {
        if (serverSocket != null)
            serverSocket.close();
        if (socket != null)
            socket.close();
        if (inputStream != null)
            inputStream.close();
        if (outputStream != null)
            outputStream.close();
        if (reader != null)
            reader.close();
        if (writer != null)
            writer.close();
    }
}
