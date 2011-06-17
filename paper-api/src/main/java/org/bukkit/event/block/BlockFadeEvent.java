package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
/**
 *Called when a block fades, melts or disappears based on world conditions
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
     * Gets the state of the block that will be fading
     *
     * @return the block state
     */
    public BlockState getNewState() {
        return newState;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param cancel true if you wish to cancel snow from forming during a ice formation
     */
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
