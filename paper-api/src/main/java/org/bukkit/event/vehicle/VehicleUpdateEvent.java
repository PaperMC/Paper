package org.bukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

public class VehicleUpdateEvent extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();

    public VehicleUpdateEvent(final Vehicle vehicle) {
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
