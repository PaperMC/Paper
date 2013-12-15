package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block fades, melts or disappears based on world conditions
 * <p>
 * Examples:
 * <ul>
 * <li>Snow melting due to being near a light source.
 * <li>Ice melting due to being near a light source.
 * <li>Fire burning out after time, without destroying fuel block.
 * </ul>
 * <p>
 * If a Block Fade event is cancelled, the block will not fade, melt or
 * disappear.
 */
public class BlockFadeEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final BlockState newState;

    public BlockFadeEvent(final Block block, final BlockState newState) {
        super(block);
        this.newState = newState;
        this.cancelled = false;
    }

    /**
     * Gets the state of the block that will be fading, melting or
     * disappearing.
     *
     * @return The block state of the block that will be fading, melting or
     *     disappearing
     */
    public BlockState getNewState() {
        return newState;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
