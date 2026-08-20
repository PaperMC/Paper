package com.destroystokyo.paper.event.player;

import io.papermc.paper.event.player.AbstractRespawnEvent;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired after a player has respawned
 */
public interface PlayerPostRespawnEvent extends AbstractRespawnEvent {

    /**
     * Returns the location of the respawned player.
     *
     * @return location of the respawned player
     * @see #getRespawnLocation()
     */
    @ApiStatus.Obsolete(since = "1.21.5")
    default Location getRespawnedLocation() {
        return this.getRespawnLocation();
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
