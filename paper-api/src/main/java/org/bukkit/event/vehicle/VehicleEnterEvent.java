package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when an entity enters a vehicle.
 * 
 * @author sk89q
 */
public class VehicleEnterEvent extends VehicleEvent implements Cancellable {
    private boolean cancelled;
    private Entity entered;

    public VehicleEnterEvent(Vehicle vehicle, Entity entered) {
        super(Type.VEHICLE_ENTER, vehicle);
        this.entered = entered;
    }

    /**
     * Get the entity that entered the vehicle.
     * 
     * @return
     */
    public Entity getEntered() {
        return entered;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
