package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

/**
 * Called when a redstone current changes
 */
public class BlockRedstoneEvent extends BlockEvent {
    private static final HandlerList handlers = new HandlerList();
    private final int oldCurrent;
    private int newCurrent;

    public BlockRedstoneEvent(final Block block, final int oldCurrent, final int newCurrent) {
        super(block);
        this.oldCurrent = oldCurrent;
        this.newCurrent = newCurrent;
    }

    /**
     * Gets the old current of this block
     *
     * @return The previous current
     */
    public int getOldCurrent() {
        return oldCurrent;
    }

    /**
     * Gets the new current of this block
     *
     * @return The new current
     */
    public int getNewCurrent() {
        return newCurrent;
    }

    /**
     * Sets the new current of this block
     *
     * @param newCurrent The new current to set
     */
    public void setNewCurrent(int newCurrent) {
        this.newCurrent = newCurrent;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
