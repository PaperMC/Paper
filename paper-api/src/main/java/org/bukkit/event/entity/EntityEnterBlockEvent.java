package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an {@link Entity} enters a block and is stored in that block.
 * <p>
 * This event is called for bees entering a bee hive.
 * <br>
 * It is not called when a silverfish "enters" a stone block. For that listen to
 * the {@link EntityChangeBlockEvent}.
 */
public interface EntityEnterBlockEvent extends EntityEvent, Cancellable {

    /**
     * Get the block the entity will enter.
     *
     * @return the block
     */
    Block getBlock();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
