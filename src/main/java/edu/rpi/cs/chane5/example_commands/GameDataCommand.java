package edu.rpi.cs.chane5.example_commands;

import edu.rpi.cs.chane5.networking.commands.ArgumentFormat;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

public class GameDataCommand extends Command {
    private int secondsBtwTurns;
    private int ships2wide;
    private int ships3wide;
    private int ships4wide;
    private int gridSize;

    private GameDataCommand() {
        super("gamedata");
        addArgument(new ArgumentBase("secondsBtwTurns", ArgumentFormat.INT));
        addArgument(new ArgumentBase("ships2wide", ArgumentFormat.INT));
        addArgument(new ArgumentBase("ships3wide", ArgumentFormat.INT));
        addArgument(new ArgumentBase("ships4wide", ArgumentFormat.INT));
        addArgument(new ArgumentBase("gridSize", ArgumentFormat.INT));
        // todo the state of the game settings from server to client
    }

    /**
     * Creates a new game data instance with the data.
     *
     * @param secondsBtwTurns seconds in between player turns otherwise automatic loss
     * @param ships2wide      number of 2 wide ships
     * @param ships3wide      number of 3 wide ships
     * @param ships4wide      number of 4 wide ships
     * @param gridSize        size of the board
     */
    public GameDataCommand(int secondsBtwTurns, int ships2wide, int ships3wide, int ships4wide, int gridSize) {
        this();
        this.secondsBtwTurns = secondsBtwTurns;
        this.ships2wide = ships2wide;
        this.ships3wide = ships3wide;
        this.ships4wide = ships4wide;
        this.gridSize = gridSize;
    }

    /**
     * Gets the seconds in between player turns otherwise automatic loss
     *
     * @return seconds in between player turns otherwise automatic loss
     */
    public int getSecondsBtwTurns() {
        return secondsBtwTurns;
    }

    /**
     * Gets the number of 2 wide ships
     *
     * @return number of 2 wide ships
     */
    public int getShips2wide() {
        return ships2wide;
    }

    /**
     * Gets the number of 3 wide ships
     *
     * @return number of 3 wide ships
     */
    public int getShips3wide() {
        return ships3wide;
    }

    /**
     * Gets the number of 4 wide ships
     *
     * @return number of 4 wide ships
     */
    public int getShips4wide() {
        return ships4wide;
    }

    /**
     * Gets the size of the board
     *
     * @return the size of the board
     */
    public int getGridSize() {
        return gridSize;
    }

    @Override
    public Command applyParser(CommandParser parser) {
        GameDataCommand gameDataCommand = new GameDataCommand();

        secondsBtwTurns = parser.getInt("secondsBtwTurns");
        ships2wide = parser.getInt("ships2wide");
        ships3wide = parser.getInt("ships3wide");
        ships4wide = parser.getInt("ships4wide");
        gridSize = parser.getInt("gridSize");
        return gameDataCommand;
    }

    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + secondsBtwTurns + DELIMITER + ships2wide + DELIMITER + ships3wide + DELIMITER + ships4wide + DELIMITER + gridSize;
    }

    @Override
    public Command newInstance() {
        return new GameDataCommand(secondsBtwTurns, ships2wide, ships3wide, ships4wide, gridSize);
    }
}
