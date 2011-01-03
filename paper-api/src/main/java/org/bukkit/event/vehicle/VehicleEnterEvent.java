package org.bukkit.event.vehicle;

import org.bukkit.LivingEntity;
import org.bukkit.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a living entity enters a vehicle.
 * 
 * @author sk89q
 */
public class VehicleEnterEvent extends VehicleEvent implements Cancellable {
    private boolean cancelled;
    private LivingEntity entered;
    
    public VehicleEnterEvent(Type type, Vehicle vehicle, LivingEntity entered) {
        super(type, vehicle);
        
        this.entered = entered;
    }
    
    /**
     * Get the living entity that entered the vehicle.
     * 
     * @return
     */
    public LivingEntity getEntered() {
        return entered;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
