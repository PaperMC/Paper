package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player toggles their flying state
 */
public interface PlayerToggleFlightEvent extends PlayerEvent, Cancellable {

    /**
     * Returns whether the player is trying to start or stop flying.
     *
     * @return flying state
     */
    boolean isFlying();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
