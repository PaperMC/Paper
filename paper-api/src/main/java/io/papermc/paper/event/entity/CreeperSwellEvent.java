package io.papermc.paper.event.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired whenever a creeper's swell level changes.
 * <p>
 * If this event is cancelled, the swell change will not be applied, but the client may still render the swell change.
 */
@NullMarked
public class CreeperSwellEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean cancelled;
    private int swellChange;
    private int finalSwellChange;
    private final int currentSwell;
    private final int maxSwell;
    private final SwellReason swellReason;

    @ApiStatus.Internal
    public CreeperSwellEvent(final Creeper creeper, int swellChange, int finalSwellChange, int currentSwell,
                             int maxSwell, SwellReason swellReason) {
        super(creeper);
        this.swellChange = swellChange;
        this.finalSwellChange = finalSwellChange;
        this.currentSwell = currentSwell;
        this.maxSwell = maxSwell;
        this.swellReason = swellReason;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return {@code true} if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel {@code true} if you wish to cancel this event
     */
    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Returns the Entity involved in this event
     *
     * @return Entity who is involved in this event
     */
    @Override
    public Creeper getEntity() {
        return (Creeper) super.getEntity();
    }

    /**
     * Gets the current swell level of the {@link Creeper}
     *
     * @return current swell level of the {@link Creeper}
     */
    public int getCurrentSwell() {
        return this.currentSwell;
    }

    /**
     * Gets the current maximum swell level of the {@link Creeper}
     *
     * @return maximum swell level of the {@link Creeper}
     */
    public int getMaxSwell() {
        return this.maxSwell;
    }

    /**
     * Gets the raw swell change before clamping
     *
     * @return the raw swell change
     */
    public int getSwellChange() {
        return this.swellChange;
    }

    /**
     * Gets the final swell change that will actually be applied to the {@link Creeper}
     *
     * @return the final swell change
     */
    public int getFinalSwellChange() {
        return this.finalSwellChange;
    }

    /**
     * Sets the swell change to apply to the {@link Creeper}
     * <p>
     * The swell change will be clamped to keep the {@link Creeper}'s swell level within [0, maxSwell]
     *
     * @param newSwellChange the swell change to apply
     * @see #getFinalSwellChange()
     */
    public void setSwellChange(int newSwellChange) {
        this.swellChange = newSwellChange;
        // don't allow creeper swell level to go below 0 or above maxSwell
        if (this.currentSwell + newSwellChange < 0) {
            this.finalSwellChange = -this.currentSwell;
        } else if (this.currentSwell + newSwellChange > this.maxSwell) {
            this.finalSwellChange = this.maxSwell - this.currentSwell;
        } else {
            this.finalSwellChange = newSwellChange;
        }
    }

    /**
     * Gets the reason for the swell change
     *
     * @return a SwellReason value detailing the cause of the swell change
     */
    public SwellReason getSwellReason() {
        return this.swellReason;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * An enum to specify the reason for a creeper swell change
     */
    public enum SwellReason {
        /**
         * Swelling caused by the creeper being primed (by proximity or ignition)
         */
        PRIMED,
        /**
         * Swelling caused by the creeper falling from a height
         */
        FALL_DAMAGE,
        /**
         * Swelling caused by custom behavior, such as a plugin
         */
        CUSTOM
    }
}
