package com.destroystokyo.paper.event.player;

import io.papermc.paper.event.player.AbstractRespawnEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired after a player has respawned
 */
@NullMarked
public class PlayerPostRespawnEvent extends AbstractRespawnEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public PlayerPostRespawnEvent(
        final Player respawnPlayer,
        final Location respawnLocation,
        final boolean isBedSpawn,
        final boolean isAnchorSpawn,
        final boolean missingRespawnBlock,
        final PlayerRespawnEvent.RespawnReason respawnReason
    ) {
        super(respawnPlayer, respawnLocation, isBedSpawn, isAnchorSpawn, missingRespawnBlock, respawnReason);
    }

    /**
     * Returns the location of the respawned player.
     *
     * @return location of the respawned player
     * @see #getRespawnLocation()
     */
    @ApiStatus.Obsolete
    public Location getRespawnedLocation() {
        return super.getRespawnLocation();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
