package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * Triggered when a player starts spectating an entity in spectator mode.
 */
public interface PlayerStartSpectatingEntityEvent extends PlayerEvent, Cancellable {

    /**
     * Gets the entity that the player is currently spectating or themselves if they weren't spectating anything
     *
     * @return The entity the player is currently spectating (before they start spectating the new target).
     */
    Entity getCurrentSpectatorTarget();

    /**
     * Gets the new entity that the player will now be spectating
     *
     * @return The entity the player is now going to be spectating.
     */
    Entity getNewSpectatorTarget();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}

