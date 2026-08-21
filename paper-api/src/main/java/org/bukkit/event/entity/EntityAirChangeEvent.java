package org.bukkit.event.entity;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called when the amount of air an entity has remaining changes.
 */
public interface EntityAirChangeEvent extends EntityEventNew, Cancellable {

    /**
     * Gets the amount of air the entity has left (measured in ticks).
     *
     * @return amount of air remaining
     */
    int getAmount();

    /**
     * Sets the amount of air remaining for the entity (measured in ticks).
     *
     * @param amount amount of air remaining
     */
    void setAmount(int amount);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
