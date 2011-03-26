package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Called on sign change
 */
public class SignChangeEvent extends BlockEvent implements Cancellable {
    private boolean cancel = false;
    private Player player;
    private String[] lines;

    public SignChangeEvent(final Block theBlock, final Player thePlayer, String[] theLines) {
        super(Type.SIGN_CHANGE, theBlock);
        this.player = thePlayer;
        this.lines = theLines;
    }

    /**
     * Gets the player for this event
     *
     * @return Player player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets all of the text lines from this event
     *
     * @return String[] of the event's text lines
     */
    public String[] getLines() {
        return lines;
    }

    /**
     * Gets a single line from this event
     *
     * @param index index of the line to get
     * @return String line
     */
    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    /**
     * Sets a single line for this event
     *
     * @param index index of the line to set
     * @param line text to set
     */
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
