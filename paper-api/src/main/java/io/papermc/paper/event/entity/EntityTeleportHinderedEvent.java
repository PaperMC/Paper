package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.ApiStatus;
import javax.annotation.Nullable;

public class EntityTeleportHinderedEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Reason reason;

    private final @Nullable PlayerTeleportEvent.TeleportCause teleportCause;

    private boolean retry = false;

    @ApiStatus.Internal
    public EntityTeleportHinderedEvent(Entity what, Reason reason, @Nullable
    PlayerTeleportEvent.TeleportCause teleportCause) {
        super(what);
        this.reason = reason;
        this.teleportCause = teleportCause;
    }

    /**
     * @return why the teleport was hindered.
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * @return why the teleport occurred if cause was given, otherwise {@code null}.
     */
    @Nullable
    public PlayerTeleportEvent.TeleportCause getTeleportCause() {
        return teleportCause;
    }

    /**
     * Whether the teleport should be retried.
     * <p>
     * Note that this can put the server in a never-ending loop of trying to teleport someone resulting in a stack
     * overflow. Do not retry more than necessary.
     * </p>
     *
     * @return whether the teleport should be retried.
     */
    public boolean shouldRetry() {
        return retry;
    }

    /**
     * Sets whether the teleport should be retried.
     * <p>
     * Note that this can put the server in a never-ending loop of trying to teleport someone resulting in a stack
     * overflow. Do not retry more than necessary.
     * </p>
     *
     * @param retry whether the teleport should be retried.
     */
    public void setShouldRetry(boolean retry) {
        this.retry = retry;
    }

    /**
     * Calls the event and tests if should retry.
     *
     * @return whether the teleport should be retried.
     */
    @Override
    public boolean callEvent() {
        super.callEvent();
        return shouldRetry();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Reason for hindrance in teleports.
     */
    public enum Reason {
        /**
         * The teleported entity is a passenger of another entity.
         */
        IS_PASSENGER,

        /**
         * The teleported entity has passengers.
         */
        IS_VEHICLE,

        /**
         * The teleport event was cancelled.
         * <p>
         * This is only caused by players teleporting.
         * </p>
         */
        EVENT_CANCELLED,
    }
}
