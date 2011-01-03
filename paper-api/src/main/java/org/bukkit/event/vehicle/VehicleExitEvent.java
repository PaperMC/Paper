package org.bukkit.event.vehicle;

import org.bukkit.LivingEntity;
import org.bukkit.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a living entity exits a vehicle.
 * 
 * @author sk89q
 */
public class VehicleExitEvent extends VehicleEvent implements Cancellable {
    private boolean cancelled;
    private LivingEntity exited;
    
    public VehicleExitEvent(Type type, Vehicle vehicle, LivingEntity exited) {
        super(type, vehicle);
        
        this.exited = exited;
    }
    
    /**
     * Get the living entity that exited the vehicle.
     * 
     * @return
     */
    public LivingEntity getExited() {
        return exited;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
