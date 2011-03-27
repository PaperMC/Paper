package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;

public class VehicleUpdateEvent extends VehicleEvent {
    public VehicleUpdateEvent(Vehicle vehicle) {
        super(Type.VEHICLE_UPDATE, vehicle);
    }
}
