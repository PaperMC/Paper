package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called immediately prior to an entity being unleashed.
 */
public class EntityUnleashEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final UnleashReason reason;
    private boolean dropLeash; // Paper

    // Paper start - drop leash variable
    @Deprecated
    public EntityUnleashEvent(@NotNull Entity entity, @NotNull UnleashReason reason) {
        this(entity, reason, false);
    }

    public EntityUnleashEvent(@NotNull Entity entity, @NotNull UnleashReason reason, boolean dropLeash) {
        super(entity);
        // Paper end
        this.reason = reason;
        this.dropLeash = dropLeash; // Paper
    }

    /**
     * Returns the reason for the unleashing.
     *
     * @return The reason
     */
    @NotNull
    public UnleashReason getReason() {
        return reason;
    }

    // Paper start
    /**
     * Returns whether a leash item will be dropped.
     *
     * @return Whether the leash item will be dropped
     */
    public boolean isDropLeash() {
        return dropLeash;
    }

    /**
     * Sets whether a leash item should be dropped.
     *
     * @param dropLeash Whether the leash item should be dropped
     */
    public void setDropLeash(boolean dropLeash) {
        this.dropLeash = dropLeash;
    }
    // Paper end

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum UnleashReason {
        /**
         * When the entity's leashholder has died or logged out, and so is
         * unleashed
         */
        HOLDER_GONE,
        /**
         * When the entity's leashholder attempts to unleash it
         */
        PLAYER_UNLEASH,
        /**
         * When the entity's leashholder is more than 10 blocks away
         */
        DISTANCE,
        UNKNOWN;
    }
}
