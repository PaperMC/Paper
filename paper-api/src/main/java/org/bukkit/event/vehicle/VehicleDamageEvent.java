package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Raised when a vehicle receives damage.
 */
public class VehicleDamageEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity attacker;
    private double damage;

    private boolean cancelled;

    @ApiStatus.Internal
    public VehicleDamageEvent(@NotNull final Vehicle vehicle, @Nullable final Entity attacker, final double damage) {
        super(vehicle);
        this.attacker = attacker;
        this.damage = damage;
    }

    /**
     * Gets the Entity that is attacking the vehicle
     *
     * @return the Entity that is attacking the vehicle
     */
    @Nullable
    public Entity getAttacker() {
        return this.attacker;
    }

    /**
     * Gets the damage done to the vehicle
     *
     * @return the damage done to the vehicle
     */
    public double getDamage() {
        return this.damage;
    }

    /**
     * Sets the damage done to the vehicle
     *
     * @param damage The damage
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
