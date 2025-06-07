package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Enderman;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class EndermanEscapeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Reason reason;
    private boolean cancelled;

    @ApiStatus.Internal
    public EndermanEscapeEvent(final Enderman entity, final Reason reason) {
        super(entity);
        this.reason = reason;
    }

    @Override
    public Enderman getEntity() {
        return (Enderman) super.getEntity();
    }

    /**
     * Gets the reason the enderman is trying to escape.
     *
     * @return The reason
     */
    public Reason getReason() {
        return this.reason;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels the escape.
     * <p>
     * If this escape normally had resulted in damage avoidance such as indirect,
     * the enderman will now take damage. However, this does not change the Enderman's
     * innate immunities or damage behavior like arrows where the damage never happens.
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

    public enum Reason {
        /**
         * The enderman has stopped attacking and ran away
         */
        RUNAWAY,
        /**
         * The enderman has teleported away due to indirect damage (ranged)
         */
        INDIRECT,
        /**
         * The enderman has teleported away due to a critical hit
         */
        CRITICAL_HIT,
        /**
         * The enderman has teleported away due to the player staring at it during combat
         */
        STARE,
        /**
         * Specific case for {@link #CRITICAL_HIT} where the enderman is taking damage by drowning (ex: rain)
         */
        DROWN
    }
}
