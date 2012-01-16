package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle receives damage.
 */
@SuppressWarnings("serial")
public class VehicleDamageEvent extends VehicleEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Entity attacker;
    private int damage;
    private boolean cancelled;

    public VehicleDamageEvent(Vehicle vehicle, Entity attacker, int damage) {
        super(Type.VEHICLE_DAMAGE, vehicle);
        this.attacker = attacker;
        this.damage = damage;
    }

    /**
     * Gets the Entity that is attacking the vehicle
     *
     * @return the Entity that is attacking the vehicle
     */
    public Entity getAttacker() {
        return attacker;
    }

    /**
     * Gets the damage done to the vehicle
     *
     * @return the damage done to the vehicle
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the damage done to the vehicle
     *
     * @param damage The damage
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
