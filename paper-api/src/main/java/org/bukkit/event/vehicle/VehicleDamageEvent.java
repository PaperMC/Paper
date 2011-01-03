package org.bukkit.event.vehicle;

import org.bukkit.Entity;
import org.bukkit.Vehicle;
import org.bukkit.event.Cancellable;

/**
 * Raised when a vehicle receives damage.
 * 
 * @author sk89q
 */
public class VehicleDamageEvent extends VehicleEvent implements Cancellable {
    private Entity attacker;
    private int damage;
    private boolean cancelled;
    
    public VehicleDamageEvent(Type type, Vehicle vehicle, Entity attacker, int damage) {
        super(type, vehicle);
        this.attacker = attacker;
        this.damage = damage;
    }
    
    public Entity getAttacker() {
        return attacker;
    }
    
    public int getDamage() {
        return damage;
    }
    
    /**
     * Change the damage.
     * 
     * @param damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
