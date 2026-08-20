package org.bukkit.event.block;

import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the fluid level of a block changes due to changes in adjacent
 * blocks.
 */
public interface FluidLevelChangeEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the new data of the changed block.
     *
     * @return new data
     */
    BlockData getNewData();

    /**
     * Sets the new data of the changed block. Must be of the same Material as
     * the old one.
     *
     * @param newData the new data
     */
    void setNewData(BlockData newData);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
