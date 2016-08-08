package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the amount of air an entity has remaining changes.
 */
public class EntityAirChangeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    //
    private int amount;
    //
    private boolean cancelled;

    public EntityAirChangeEvent(Entity what, int amount) {
        super(what);
        this.amount = amount;
    }

    /**
     * Gets the amount of air the entity has left (measured in ticks).
     *
     * @return amount of air remaining
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of air remaining for the entity (measured in ticks.
     *
     * @param amount amount of air remaining
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
