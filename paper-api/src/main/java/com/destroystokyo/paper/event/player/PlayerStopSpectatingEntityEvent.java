package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Triggered when a player stops spectating an entity in spectator mode.
 */
@NullMarked
public interface PlayerStopSpectatingEntityEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets the entity that the player is spectating
     *
     * @return The entity the player is currently spectating (before they will stop).
     */
    Entity getSpectatorTarget();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
