package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

public class CraftVehicleUpdateEvent extends CraftVehicleEvent implements VehicleUpdateEvent {

    public CraftVehicleUpdateEvent(final Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public HandlerList getHandlers() {
        return VehicleUpdateEvent.getHandlerList();
    }
}
