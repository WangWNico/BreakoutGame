package edu.rpi.cs.chane5.networking.commands;

public class TestCommand extends Command {
    private char argC;
    private int argI;
    private double argD;
    private String argS;

    {
        // set this example_commands arguments
        addArgument(new ArgumentBase("c", ArgumentFormat.CHAR));
        addArgument(new ArgumentBase("i", ArgumentFormat.INT));
        addArgument(new ArgumentBase("d", ArgumentFormat.DOUBLE));
        addArgument(new ArgumentBase("s", ArgumentFormat.STRING));
    }

    public TestCommand() {
        this('c', 0, 0.0, "string");
    }

    public TestCommand(char c, int i, double d, String s) {
        super("test");
        argC = c;
        argI = i;
        argD = d;
        argS = s;
    }

    public char getArgC() {
        return argC;
    }

    public int getArgI() {
        return argI;
    }

    public double getArgD() {
        return argD;
    }

    public String getArgS() {
        return argS;
    }

    @Override
    public TestCommand applyParser(CommandParser parser) {
        argC = parser.getChar("c");
        argI = parser.getInt("i");
        argD = parser.getDouble("d");
        argS = parser.getString("s");
        return this;
    }

    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + argC + DELIMITER + argI + DELIMITER + argD + DELIMITER + argS;
    }

    @Override
    public Command newInstance() {
        return new TestCommand();
    }

}
