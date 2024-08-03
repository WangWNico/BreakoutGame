package edu.rpi.cs.chane5.networking.connection;

import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.ErrorCommand;
import edu.rpi.cs.chane5.networking.commands.Registry;
import edu.rpi.cs.chane5.networking.commands.TestCommand;
import org.junit.jupiter.api.Test;

import java.net.BindException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class NetworkingTests {


    private Server server;
    private Client client;
    private Thread threadServer;
    private Thread threadClient;
    private CountDownLatch latch = new CountDownLatch(2);

    @Test
    void mainTestEntry() throws Exception {
        System.out.println("init test ");

        Registry.get().register(new ErrorCommand());


        this.threadClient = new Thread(() -> {
            code(false);
        });
        this.threadServer = new Thread(() -> {
            code(true);
        });
        System.out.println("4\t");

        threadServer.start();
        System.out.println("5\t");
        threadClient.start();
        System.out.println("6\t");

        latch.await();
        System.out.println("END OF LATCH");

        Server serverAltPort = new Server(25565);
        assertEquals(25565, serverAltPort.getPort());

        assertThrows(BindException.class, () -> new Server(25565));
        System.out.println("dupe");
    }


    private void code(boolean server) {
        try {
            if (server) {
                serverCode();
            } else {
                clientCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void serverCode() throws Exception {
        System.out.println("server- starting server code");

        this.server = new Server();
        System.out.println("server - started");

        assertEquals(Connection.DEFAULT_PORT, server.getPort());

        server.connect();
        System.out.println("server - connected");

        // msg 1
        server.send(new ErrorCommand());
        System.out.println("server - sent server command");

        // msg 2
        server.send(new TestCommand());

        // msg 3
        Command cmd = server.receive();
        assertEquals(new ErrorCommand().getName(), cmd.getName());

        // msg 4
        Registry.get().register(new TestCommand());
        server.send(new TestCommand('w', 7, 9.1, "string1"));

        // msg 5
        TestCommand testCommand = (TestCommand) server.receive();
        assertEquals('p', testCommand.getArgC());
        assertEquals(451, testCommand.getArgI());
        assertEquals(89.92, testCommand.getArgD());
        assertEquals("big", testCommand.getArgS());

        latch.countDown();
        server.close();
    }

    private void clientCode() throws Exception {
        System.out.println("client - starting client code");

        this.client = new Client();
        System.out.println("client - started");

        assertEquals(Connection.DEFAULT_PORT, client.getPort());

        client.connect();
        System.out.println("client - connected");

        // msg 1
        System.out.println("client - waiting for receive");
        Command remoteInput = client.receive();
        System.out.println("client - received client code");
        assertNotNull(remoteInput);
        ErrorCommand errorCommand = new ErrorCommand();
        assertEquals(errorCommand.getName(), remoteInput.getName());
        System.out.println(">>>\t" + errorCommand);
        assertNotSame(errorCommand, errorCommand.newInstance());

        // msg 2
        Command helpCMD = client.receive();
        assertNull(helpCMD);

        // msg 3
        client.send(new ErrorCommand());

        // msg 4
        TestCommand cmd = (TestCommand) client.receive();
        assertEquals('w', cmd.getArgC());
        assertEquals(7, cmd.getArgI());
        assertEquals(9.1, cmd.getArgD());
        assertEquals("string1", cmd.getArgS());
        assertNotSame(cmd, cmd.newInstance());

        // msg 5
        client.send(new TestCommand('p', 451, 89.92, "big"));

        latch.countDown();
        client.close();
    }
}
