package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class CraftVehicleMoveEvent extends CraftVehicleEvent implements VehicleMoveEvent {

    private final Location from;
    private final Location to;

    public CraftVehicleMoveEvent(final Vehicle vehicle, final Location from, final Location to) {
        super(vehicle);

        this.from = from;
        this.to = to;
    }

    @Override
    public Location getFrom() {
        return this.from.clone();
    }

    @Override
    public Location getTo() {
        return this.to.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return VehicleMoveEvent.getHandlerList();
    }
}
