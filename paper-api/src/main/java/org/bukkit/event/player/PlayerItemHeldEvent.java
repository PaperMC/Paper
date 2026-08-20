package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Fired when a player changes their currently held item
 */
public interface PlayerItemHeldEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the previous held slot index
     *
     * @return Previous slot index
     */
    int getPreviousSlot();

    /**
     * Gets the new held slot index
     *
     * @return New slot index
     */
    int getNewSlot();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
