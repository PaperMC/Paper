package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle is created.
 */
public class VehicleCreateEvent extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();

    public VehicleCreateEvent(final Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
