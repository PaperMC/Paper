package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when the server is calculating what chunks to try to spawn monsters in every Monster Spawn Tick event
 */
@NullMarked
public class PlayerNaturallySpawnCreaturesEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private byte radius;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerNaturallySpawnCreaturesEvent(final Player player, final byte radius) {
        super(player);
        this.radius = radius;
    }

    /**
     * @return The radius of chunks around this player to be included in natural spawn selection
     */
    public byte getSpawnRadius() {
        return this.radius;
    }

    /**
     * @param radius The radius of chunks around this player to be included in natural spawn selection
     */
    public void setSpawnRadius(final byte radius) {
        this.radius = radius;
    }

    /**
     * @return If this player's chunks will be excluded from natural spawns
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * @param cancel {@code true} if you wish to cancel this event, and not include this player's chunks for natural spawning
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
