package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Raised when a vehicle receives damage.
 */
public class VehicleDamageEvent extends VehicleEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Entity attacker;
    private int damage;
    private boolean cancelled;

    public VehicleDamageEvent(final Vehicle vehicle, final Entity attacker, final int damage) {
        super(vehicle);
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
