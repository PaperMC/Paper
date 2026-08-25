package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

/**
 * Called when an entity interacts with an object
 */
public interface EntityInteractEvent extends EntityEvent, BlockEvent, Cancellable {

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
