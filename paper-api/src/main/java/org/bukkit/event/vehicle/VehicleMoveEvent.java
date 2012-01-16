package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle moves.
 */
@SuppressWarnings("serial")
public class VehicleMoveEvent extends VehicleEvent {
    private static final HandlerList handlers = new HandlerList();
    private Location from;
    private Location to;

    public VehicleMoveEvent(Vehicle vehicle, Location from, Location to) {
        super(Type.VEHICLE_MOVE, vehicle);

        this.from = from;
        this.to = to;
    }

    /**
     * Get the previous position.
     *
     * @return Old position.
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Get the next position.
     *
     * @return New position.
     */
    public Location getTo() {
        return to;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
