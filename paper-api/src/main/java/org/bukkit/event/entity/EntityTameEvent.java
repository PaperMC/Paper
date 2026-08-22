package org.bukkit.event.entity;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Thrown when a living entity is tamed.
 */
public interface EntityTameEvent extends EntityEvent, Cancellable {

    @Override
    LivingEntity getEntity();

    /**
     * Gets the owning AnimalTamer
     *
     * @return the owning AnimalTamer
     */
    AnimalTamer getOwner();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
