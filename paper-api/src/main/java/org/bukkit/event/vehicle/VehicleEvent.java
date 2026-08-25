package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.Event;

public interface VehicleEvent extends Event {

    /**
     * Get the vehicle.
     *
     * @return the vehicle
     */
    Vehicle getVehicle();
}
