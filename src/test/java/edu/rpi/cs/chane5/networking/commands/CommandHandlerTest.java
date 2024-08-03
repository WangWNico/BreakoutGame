package edu.rpi.cs.chane5.networking.commands;

import org.junit.jupiter.api.Test;

import static edu.rpi.cs.chane5.networking.commands.Command.DELIMITER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandHandlerTest {
    private void runTestCommandData(char c, int i, double d, String s) {
        TestCommand cmd = new TestCommand(c, i, d, s);
        assertEquals("test", cmd.getName());
        assertEquals(cmd.getName() + DELIMITER + c + DELIMITER + i + DELIMITER + d + DELIMITER + s, cmd.getParsedCommand());
        assertEquals("test" + DELIMITER + "c" + DELIMITER + "i" + DELIMITER + "d" + DELIMITER + "s", cmd.toString());
    }

    private void runHandler() {
        CommandParser parser = CommandParser.parse(new TestCommand(), "x" + DELIMITER + "25" + DELIMITER + "3.14" + DELIMITER + "string");
        if (!parser.isSuccessful()) {
            System.err.println("error");
            return;
        }

        assertTrue(parser.isSuccessful());
        assertEquals('x', parser.getChar("c"));
        assertEquals(25, parser.getInt("i"));
        assertEquals(3.14, parser.getDouble("d"));
        assertEquals("string", parser.getString("s"));

        TestCommand new1 = new TestCommand();
        new1.applyParser(parser);
        assertEquals('x', new1.getArgC());
        assertEquals(25, new1.getArgI());
        assertEquals(3.14, new1.getArgD());
        assertEquals("string", new1.getArgS());

        parser = CommandParser.parse(new TestCommand(), "5" + DELIMITER + "3" + DELIMITER + "78.9" + DELIMITER + "no");
        if (!parser.isSuccessful()) {
            System.err.println("error");
            return;
        }

        assertTrue(parser.isSuccessful());
        assertEquals('5', parser.getChar("c"));
        assertEquals(3, parser.getInt("i"));
        assertEquals(78.9, parser.getDouble("d"));
        assertEquals("no", parser.getString("s"));

        new1 = new TestCommand();
        new1.applyParser(parser);
        assertEquals('5', new1.getArgC());
        assertEquals(3, new1.getArgI());
        assertEquals(78.9, new1.getArgD());
        assertEquals("no", new1.getArgS());
    }

    @Test
    void testTestCommand() {
        runTestCommandData('X', 25, 3.14, "string");
        runTestCommandData('q', 43, 6.420, "bruhv");
        runHandler();
        ErrorCommand errorCommand = new ErrorCommand();
        assertEquals("error", errorCommand.getParsedCommand());
        assertEquals("error", errorCommand.getName());
        assertEquals("error", errorCommand.toString());
        assertEquals(0, errorCommand.getArgumentCount());
    }

    @Test
    void copyTest() throws Exception {
        Command cmd = new ErrorCommand();
        Command output = cmd.getClass().getDeclaredConstructor().newInstance();
        assertEquals("error", output.getName());
    }
}