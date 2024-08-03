package edu.rpi.cs.chane5.example_commands;

import edu.rpi.cs.chane5.networking.commands.ArgumentFormat;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * Command for sending a shoot to the enemy. <br />
 * Schema: <code>shoot row col</code>
 *
 * @version {@value edu.rpi.cs.chane5.battleship.BattleshipMainController#VERSION}
 */
public class ShootCommand extends Command {
    private int row;
    private int col;

    /**
     * Creates a new {@link ShootCommand} command.
     *
     * @param row the row of the shot cell
     * @param col the column of the shoot cell
     */
    public ShootCommand(int row, int col) {
        this();
        this.row = row;
        this.col = col;
    }

    // Default sets name and arguments.
    private ShootCommand() {
        super("shoot");
        addArgument(new ArgumentBase("row", ArgumentFormat.INT));
        addArgument(new ArgumentBase("col", ArgumentFormat.INT));
    }

    /**
     * Gets the shot cell's row
     *
     * @return the shot cell's row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the shot cell's column
     *
     * @return the shot cell's column
     */
    public int getCol() {
        return col;
    }

    @Override
    public ShootCommand applyParser(CommandParser parser) {
        row = parser.getInt("row");
        col = parser.getInt("col");
        return this;
    }

    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + row + DELIMITER + col;
    }

    @Override
    public ShootCommand newInstance() {
        return new ShootCommand(row, col);
    }
}
