package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;

/**
 * Raised when a vehicle is created.
 * 
 * @author sk89q
 */
public class VehicleCreateEvent extends VehicleEvent {
    public VehicleCreateEvent(Type type, Vehicle vehicle) {
        
        super(type, vehicle);
    }
}
