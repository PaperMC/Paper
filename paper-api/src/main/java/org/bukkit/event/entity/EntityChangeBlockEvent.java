package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when any entity changes a block and a more specific event is not available.
 */
public interface EntityChangeBlockEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the block the entity is changing
     *
     * @return the block that is changing
     */
    Block getBlock();

    /**
     * Gets the Material that the block is changing into
     *
     * @return the material that the block is changing into
     */
    Material getTo();

    /**
     * Gets the data for the block that would be changed into
     *
     * @return the data for the block that would be changed into
     */
    BlockData getBlockData();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
