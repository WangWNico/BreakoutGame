package edu.rpi.cs.chane5.example_commands;

import edu.rpi.cs.chane5.networking.commands.ArgumentFormat;
import edu.rpi.cs.chane5.networking.commands.Command;
import edu.rpi.cs.chane5.networking.commands.CommandParser;

/**
 * Command for replying to a shoot command. <br />
 * Schema: <code>reply row col response</code>
 *
 * @version {@value edu.rpi.cs.chane5.battleship.BattleshipMainController#VERSION}
 */
public class ReplyCommand extends Command {
    private int row;
    private int col;
    private boolean hit;

    /**
     * Creates a new {@link ReplyCommand} command.
     *
     * @param row the row of the reply cell
     * @param col the column of the reply cell
     * @param hit true if it was a hit, false if it was a miss
     */
    public ReplyCommand(int row, int col, boolean hit) {
        this();
        this.row = row;
        this.col = col;
        this.hit = hit;
    }

    // Default sets name and arguments.
    private ReplyCommand() {
        super("reply");
        addArgument(new ArgumentBase("row", ArgumentFormat.INT));
        addArgument(new ArgumentBase("col", ArgumentFormat.INT));
        addArgument(new ArgumentBase("response", ArgumentFormat.BOOL));
    }

    @Override
    public ReplyCommand applyParser(CommandParser parser) {
        hit = parser.getBoolean("response");
        row = parser.getInt("row");
        col = parser.getInt("col");
        return this;
    }

    @Override
    public String getParsedCommand() {
        return getName() + DELIMITER + row + DELIMITER + col + DELIMITER + (hit ? "true" : "false");
    }

    @Override
    public ReplyCommand newInstance() {
        return new ReplyCommand(row, col, hit);
    }

    /**
     * Is the reply command a hit?
     *
     * @return true if a hit, false if a miss
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * Gets the row of the reply cell.
     *
     * @return the row of the reply cell
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of the reply cell.
     *
     * @return the column of the reply cell
     */
    public int getCol() {
        return col;
    }
}
