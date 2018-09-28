package com.destroystokyo.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Triggered when a player starts spectating an entity in spectator mode.
 */
@NullMarked
public class PlayerStartSpectatingEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity currentSpectatorTarget;
    private final Entity newSpectatorTarget;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerStartSpectatingEntityEvent(final Player player, final Entity currentSpectatorTarget, final Entity newSpectatorTarget) {
        super(player);
        this.currentSpectatorTarget = currentSpectatorTarget;
        this.newSpectatorTarget = newSpectatorTarget;
    }

    /**
     * Gets the entity that the player is currently spectating or themselves if they weren't spectating anything
     *
     * @return The entity the player is currently spectating (before they start spectating the new target).
     */
    public Entity getCurrentSpectatorTarget() {
        return this.currentSpectatorTarget;
    }

    /**
     * Gets the new entity that the player will now be spectating
     *
     * @return The entity the player is now going to be spectating.
     */
    public Entity getNewSpectatorTarget() {
        return this.newSpectatorTarget;
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

