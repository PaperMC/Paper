package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class CraftVehicleCreateEvent extends CraftVehicleEvent implements VehicleCreateEvent {

    private boolean cancelled;

    public CraftVehicleCreateEvent(final Vehicle vehicle) {
        super(vehicle);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return VehicleCreateEvent.getHandlerList();
    }
}
