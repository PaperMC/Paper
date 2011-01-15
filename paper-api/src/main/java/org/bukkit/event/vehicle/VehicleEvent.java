package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;

/**
 * Represents a vehicle-related event.
 * 
 * @author sk89q
 */
public class VehicleEvent extends Event {
    protected Vehicle vehicle;

    public VehicleEvent(final Event.Type type, final Vehicle vehicle) {
        super(type);
        this.vehicle = vehicle;
    }

    /**
     * Get the vehicle.
     * 
     * @return the vehicle
     */
    public final Vehicle getVehicle() {
        return vehicle;
    }
}
