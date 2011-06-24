package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

/**
 * Stores data for health-regain events
 */
public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {

    private boolean cancelled;
    private int amount;

    public EntityRegainHealthEvent(Entity entity, int amount) {
        super(Event.Type.ENTITY_REGAIN_HEALTH, entity);
        this.amount = amount;
    }

    protected EntityRegainHealthEvent(Event.Type type, Entity entity, int amount) {
        super(type, entity);
        this.amount = amount;
    }

    /**
     * Gets the amount of regained health
     * @return The amount of health regained
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of regained health
     *
     * @param amount the amount of health the entity will regain
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a health-regain event is cancelled, the entity won't get health.
     * This will not fire an event.
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * If a health-regain event is cancelled, the entity won't get health.
     * This will not fire an event.
     *
     * @param cancel true if you wish to cancel this event
     */
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
