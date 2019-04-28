package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a sign is changed by a player.
 * <p>
 * If a Sign Change event is cancelled, the sign will not be changed.
 */
public class SignChangeEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final Player player;
    private final String[] lines;

    public SignChangeEvent(@NotNull final Block theBlock, @NotNull final Player thePlayer, @NotNull final String[] theLines) {
        super(theBlock);
        this.player = thePlayer;
        this.lines = theLines;
    }

    /**
     * Gets the player changing the sign involved in this event.
     *
     * @return the Player involved in this event
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets all of the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     */
    @NotNull
    public String[] getLines() {
        return lines;
    }

    /**
     * Gets a single line of text from the sign involved in this event.
     *
     * @param index index of the line to get
     * @return the String containing the line of text associated with the
     *     provided index
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     */
    @Nullable
    public String getLine(int index) throws IndexOutOfBoundsException {
        return lines[index];
    }

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     */
    public void setLine(int index, @Nullable String line) throws IndexOutOfBoundsException {
        lines[index] = line;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
