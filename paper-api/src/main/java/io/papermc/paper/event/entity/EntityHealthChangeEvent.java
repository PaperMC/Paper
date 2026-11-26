package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Damageable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when an {@link org.bukkit.entity.Damageable} health is changed.
 */
@NullMarked
public class EntityHealthChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final double oldHealth;
    private double newHealth;

    @ApiStatus.Internal
    public EntityHealthChangeEvent(final Damageable entity, double oldHealth, double newHealth) {
        super(entity);
        this.oldHealth = oldHealth;
        this.newHealth = newHealth;
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

    @Override
    public Damageable getEntity() {
        return (Damageable) super.getEntity();
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Gets the new after the change.
     *
     * @return The new health.
     */
    public double getNewHealth() {
        return newHealth;
    }

    /**
     * Gets the health before the change.
     *
     * @return The old health.
     */
    public double getOldHealth() {
        return oldHealth;
    }

    /**
     * Sets the health after the change.
     *
     * @param newHealth The new health.
     */
    public void setNewHealth(final double newHealth) {
        Preconditions.checkArgument(!Double.isNaN(newHealth) && newHealth >= 0, "New health must be non NaN and greater than or equal to 0");
        this.newHealth = newHealth;
    }
}
