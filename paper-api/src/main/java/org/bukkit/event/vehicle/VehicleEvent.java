package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;

/**
 * Represents a vehicle-related event.
 */
public abstract class VehicleEvent extends Event {
    protected Vehicle vehicle;

    public VehicleEvent(final Vehicle vehicle) {
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
