package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;

/**
 * Raised when a vehicle is created.
 * 
 * @author sk89q
 */
public class VehicleCreateEvent extends VehicleEvent {
    public VehicleCreateEvent(Vehicle vehicle) {
        super(Type.VEHICLE_CREATE, vehicle);
    }
}
