package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a block grows naturally in the world.
 * <p />
 * Examples:
 * <ul>
 * <li>Wheat</li>
 * <li>Sugar Cane</li>
 * <li>Cactus</li>
 * <li>Watermelon</li>
 * <li>Pumpkin</li>
 * </ul>
 * <p />
 * If a Block Grow event is cancelled, the block will not grow.
 */
public class BlockGrowEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final BlockState newState;
    private boolean cancelled = false;

    public BlockGrowEvent(final Block block, final BlockState newState) {
        super(block);
        this.newState = newState;
    }

    /**
     * Gets the state of the block where it will form or spread to.
     *
     * @return The block state for this events block
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

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
