package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleEvent;

public abstract class CraftVehicleEvent extends CraftEvent implements VehicleEvent {

    protected Vehicle vehicle;

    protected CraftVehicleEvent(final Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public Vehicle getVehicle() {
        return this.vehicle;
    }
}
