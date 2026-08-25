package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleCollisionEvent;

public abstract class CraftVehicleCollisionEvent extends CraftVehicleEvent implements VehicleCollisionEvent {

    protected CraftVehicleCollisionEvent(final Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public HandlerList getHandlers() {
        return VehicleCollisionEvent.getHandlerList();
    }
}
