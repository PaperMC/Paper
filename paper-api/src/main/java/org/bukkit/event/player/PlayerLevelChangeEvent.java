package org.bukkit.event.player;

import org.bukkit.event.HandlerList;

/**
 * Called when a players level changes
 */
public interface PlayerLevelChangeEvent extends PlayerEvent {

    /**
     * Gets the old level of the player
     *
     * @return The old level of the player
     */
    int getOldLevel();

    /**
     * Gets the new level of the player
     *
     * @return The new (current) level of the player
     */
    int getNewLevel();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
