package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when an entity interacts with an object
 */
public interface EntityInteractEvent extends EntityEventNew, Cancellable {

    /**
     * Returns the involved block
     *
     * @return the block clicked with this item.
     */
    Block getBlock();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
