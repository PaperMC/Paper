package org.bukkit.event.entity;

import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when an entity picks an item up from the ground
 */
public interface EntityPickupItemEvent extends EntityEventNew, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the Item picked up by the entity.
     *
     * @return Item
     */
    Item getItem();

    /**
     * Gets the amount remaining on the ground, if any
     *
     * @return amount remaining on the ground
     */
    int getRemaining();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
