package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

/**
 * Called immediately prior to an entity being unleashed.
 */
public class EntityUnleashEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final UnleashReason reason;

    public EntityUnleashEvent(Entity entity, UnleashReason reason) {
        super(entity);
        this.reason = reason;
    }

    /**
     * Returns the reason for the unleashing.
     *
     * @return The reason
     */
    public UnleashReason getReason() {
        return reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

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
