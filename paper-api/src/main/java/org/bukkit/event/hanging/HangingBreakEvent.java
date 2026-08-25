package org.bukkit.event.hanging;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Triggered when a hanging entity is removed
 */
public interface HangingBreakEvent extends HangingEvent, Cancellable {

    /**
     * Gets the cause for the hanging entity's removal
     *
     * @return the RemoveCause for the hanging entity's removal
     */
    HangingBreakEvent.RemoveCause getCause();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }

    /**
     * An enum to specify the cause of the removal
     */
    enum RemoveCause {
        /**
         * Removed by an entity
         */
        ENTITY,
        /**
         * Removed by an explosion
         */
        EXPLOSION,
        /**
         * Removed by placing a block on it
         */
        OBSTRUCTION,
        /**
         * Removed by destroying the block behind it, etc
         */
        PHYSICS,
        /**
         * Removed by an uncategorised cause
         */
        DEFAULT,
    }
}
