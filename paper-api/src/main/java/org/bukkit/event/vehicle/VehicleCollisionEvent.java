package org.bukkit.event.vehicle;

import org.bukkit.Vehicle;

/**
 * Raised when a vehicle collides.
 * 
 * @author sk89q
 */
public class VehicleCollisionEvent extends VehicleEvent {
    public VehicleCollisionEvent(Type type, Vehicle vehicle) {
        super(type, vehicle);
    }
}
