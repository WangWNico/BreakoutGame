package edu.rpi.cs.chane5.networking.connection;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;
import edu.rpi.cs.chane5.networking.commands.Registry;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

import static edu.rpi.cs.chane5.Utils.debug;

/**
 * Creates a Client for listening to data from a server. This only supports a connection to one server. The protocol is
 * determined by the various commands registered in the {@link Registry}.
 *
 * @version {@value Connection#VERSION}
 */
public class Client extends Connection {
    private final String ip;
    private final Registry registry;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Scanner reader;
    private PrintWriter writer;

    /**
     * Creates a client with the specified IP and port.
     *
     * @param serverIP the address to the server
     * @param port     the port of the server
     */
    public Client(String serverIP, int port) {
        super(port);
        this.ip = serverIP;
        this.registry = Registry.get();
    }

    /**
     * Creates a client connecting to this local machine using the default port.
     */
    public Client() {
        this("localhost", DEFAULT_PORT);
    }

    /**
     * Establishes the connection and opens all appropriate streams.
     *
     * @throws Exception An error occurred creating the connection or stream error.
     */
    @Override
    public void connect() throws Exception {
        this.socket = new Socket(this.ip, this.port);
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
        String input = reader.nextLine();
        debug(input);

        // substring off by first space delimiter
        int firstDelimiter = input.indexOf(Command.DELIMITER);
        Command cmd = registry.get(input.substring(0, firstDelimiter == -1 ? input.length() : firstDelimiter));

        // null not found
        if (cmd == null)
            return null;

        // only command header, no args
        if (firstDelimiter == -1)
            return cmd;

        // parse args
        CommandParser parser = CommandParser.parse(cmd, input.substring(firstDelimiter + 1));
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
     * Closes the client connection.
     *
     * @throws IOException when there is an I/O error
     */
    @Override
    public void close() throws Exception {
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
