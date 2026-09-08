package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntitySpawnEvent;

/**
 * Raised when a vehicle is created.
 */
public interface VehicleCreateEvent extends EntitySpawnEvent, VehicleEvent {

    @Override
    default Vehicle getEntity() {
        return this.getVehicle();
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
