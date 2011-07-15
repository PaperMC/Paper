package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
/**
 * Called when a block fades, melts or disappears based on world conditions
 * <p />
 * Examples:
 * <ul>
 *     <li>Snow melting due to being near a light source.</li>
 *     <li>Ice melting due to being near a light source.</li>
 * </ul>
 * <p />
 * If a Block Fade event is cancelled, the block will not fade, melt or disappear.
 */
public class BlockFadeEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;
    private BlockState newState;

    public BlockFadeEvent(Block block, BlockState newState) {
        super(Type.BLOCK_FADE, block);
        this.newState = newState;
        this.cancelled = false;
    }

    /**
     * Gets the state of the block that will be fading, melting or disappearing.
     *
     * @return The block state of the block that will be fading, melting or disappearing
     */
    public BlockState getNewState() {
        return newState;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *<p />
     * If a Block Fade event is cancelled, the block will not fade, melt or disappear.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *<p />
     * If a Block Fade event is cancelled, the block will not fade, melt or disappear.
     *
     * @param cancel true if you wish to cancel blocks like snow or ice from melting or fading
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
