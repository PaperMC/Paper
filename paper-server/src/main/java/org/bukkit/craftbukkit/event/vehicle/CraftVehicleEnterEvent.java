package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleEnterEvent;

public class CraftVehicleEnterEvent extends CraftVehicleEvent implements VehicleEnterEvent {

    private final Entity entered;
    private boolean cancelled;

    public CraftVehicleEnterEvent(final Vehicle vehicle, final Entity entered) {
        super(vehicle);
        this.entered = entered;
    }

    @Override
    public Entity getEntered() {
        return this.entered;
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
        return VehicleEnterEvent.getHandlerList();
    }
}
