package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a sign is changed by a player.
 * <p />
 * If a Sign Change event is cancelled, the sign will not be changed.
 */
public class SignChangeEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Player player;
    private final String[] lines;

    public SignChangeEvent(final Block theBlock, final Player thePlayer, final String[] theLines) {
        super(theBlock);
        this.player = thePlayer;
        this.lines = theLines;
    }

    /**
     * Gets the player changing the sign involved in this event.
     *
     * @return The Player involved in this event.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets all of the lines of text from the sign involved in this event.
     *
     * @return A String[] of the sign's lines of text
     */
    public String[] getLines() {
        return lines;
    }

    /**
     * Gets a single line of text from the sign involved in this event.
     *
     * @param index index of the line to get
     * @return The String containing the line of text associated with the provided index
     * @throws IndexOutOfBoundsException thrown when the provided index is > 4 and < 0
     */
    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is > 4 and < 0
     */
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
