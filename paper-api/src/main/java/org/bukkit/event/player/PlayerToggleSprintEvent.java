package org.bukkit.event.player;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when a player toggles their sprinting state
 */
public interface PlayerToggleSprintEvent extends PlayerEvent, Cancellable {

    /**
     * Gets whether the player is now sprinting or not.
     *
     * @return sprinting state
     */
    boolean isSprinting();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
