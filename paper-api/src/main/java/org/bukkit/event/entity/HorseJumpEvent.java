package org.bukkit.event.entity;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a horse jumps.
 */
public class HorseJumpEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private float power;

    public HorseJumpEvent(@NotNull final AbstractHorse horse, final float power) {
        super(horse);
        this.power = power;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @deprecated horse jumping was moved client side.
     */
    @Override
    @Deprecated
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @NotNull
    @Override
    public AbstractHorse getEntity() {
        return (AbstractHorse) entity;
    }

    /**
     * Gets the power of the jump.
     * <p>
     * Power is a value that defines how much of the horse's jump strength
     * should be used for the jump. Power is effectively multiplied times
     * the horse's jump strength to determine how high the jump is; 0
     * represents no jump strength while 1 represents full jump strength.
     * Setting power to a value above 1 will use additional jump strength
     * that the horse does not usually have.
     * <p>
     * Power does not affect how high the horse is capable of jumping, only
     * how much of its jumping capability will be used in this jump. To set
     * the horse's overall jump strength, see {@link
     * AbstractHorse#setJumpStrength(double)}.
     *
     * @return jump strength
     */
    public float getPower() {
        return power;
    }

    /**
     * Sets the power of the jump.
     * <p>
     * Jump power can be set to a value above 1.0 which will increase the
     * strength of this jump above the horse's actual jump strength.
     * <p>
     * Setting the jump power to 0 will result in the jump animation still
     * playing, but the horse not leaving the ground. Only canceling this
     * event will result in no jump animation at all.
     *
     * @param power power of the jump
     * @deprecated horse jumping was moved client side.
     */
    @Deprecated
    public void setPower(float power) {
        this.power = power;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
