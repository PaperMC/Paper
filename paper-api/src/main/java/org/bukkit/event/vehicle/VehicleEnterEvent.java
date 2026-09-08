package org.bukkit.event.vehicle;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when an entity enters a vehicle.
 */
public interface VehicleEnterEvent extends VehicleEvent, Cancellable {

    /**
     * Gets the Entity that entered the vehicle.
     *
     * @return the Entity that entered the vehicle
     */
    LivingEntity getEntered();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
