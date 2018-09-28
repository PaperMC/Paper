package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Triggered when a player stops spectating an entity in spectator mode.
 */
@NullMarked
public class PlayerStopSpectatingEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity spectatorTarget;
    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerStopSpectatingEntityEvent(final Player player, final Entity spectatorTarget) {
        super(player);
        this.spectatorTarget = spectatorTarget;
    }

    /**
     * Gets the entity that the player is spectating
     *
     * @return The entity the player is currently spectating (before they will stop).
     */
    public Entity getSpectatorTarget() {
        return this.spectatorTarget;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

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
