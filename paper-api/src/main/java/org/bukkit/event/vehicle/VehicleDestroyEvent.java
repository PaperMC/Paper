package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a vehicle is destroyed
 *
 * @author sk89q, cyklo
 */
public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {
    private Entity attacker;
    private boolean cancelled;
    
    public VehicleDestroyEvent(Vehicle vehicle, Entity attacker) {
        super(Type.VEHICLE_DESTROY, vehicle);
        this.attacker = attacker;
    }
    
    public Entity getAttacker() {
        return attacker;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
