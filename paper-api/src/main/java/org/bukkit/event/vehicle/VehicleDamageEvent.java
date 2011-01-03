package org.bukkit.event.vehicle;

import org.bukkit.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a vehicle receives damage.
 * 
 * @author sk89q
 */
public class VehicleDamageEvent extends VehicleEvent implements Cancellable {
    private boolean cancelled;
    
    public VehicleDamageEvent(Type type, Vehicle vehicle) {
        super(type, vehicle);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
