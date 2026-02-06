package org.bukkit.event.player;

import org.bukkit.World;
import org.bukkit.event.HandlerList;

/**
 * Called when a player switches to another world.
 */
public interface PlayerChangedWorldEvent extends PlayerEvent {

    /**
     * Gets the world the player is switching from.
     *
     * @return player's previous world
     */
    World getFrom();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
