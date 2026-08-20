package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player toggles their sneaking state
 */
public interface PlayerToggleSneakEvent extends PlayerEvent, Cancellable {

    /**
     * Returns whether the player is now sneaking or not.
     *
     * @return sneaking state
     */
    boolean isSneaking();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
