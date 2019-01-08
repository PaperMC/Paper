package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.Warning;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the fluid level of a block changes due to changes in adjacent
 * blocks.
 *
 * @deprecated draft API
 */
@Deprecated
@Warning(false)
public class FluidLevelChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private BlockData newData;

    public FluidLevelChangeEvent(Block theBlock, BlockData newData) {
        super(theBlock);
        this.newData = newData;
    }

    /**
     * Gets the new data of the changed block.
     *
     * @return new data
     */
    public BlockData getNewData() {
        return newData;
    }

    /**
     * Sets the new data of the changed block. Must be of the same Material as
     * the old one.
     *
     * @param newData the new data
     */
    public void setNewData(BlockData newData) {
        Preconditions.checkArgument(newData != null, "newData null");
        Preconditions.checkArgument(this.newData.getMaterial().equals(newData.getMaterial()), "Cannot change fluid type");

        this.newData = newData;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
