package org.bukkit.event.vehicle;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Raised when a vehicle receives damage.
 */
public interface VehicleDamageEvent extends VehicleEvent, Cancellable {

    /**
     * Gets the DamageSource that caused the damage.
     *
     * @return the DamageSource that caused the damage
     */
    DamageSource getDamageSource();

    /**
     * Gets the Entity that is attacking the vehicle
     *
     * @return the Entity that is attacking the vehicle
     */
    @Nullable Entity getAttacker();

    /**
     * Gets the damage done to the vehicle
     *
     * @return the damage done to the vehicle
     */
    double getDamage();

    /**
     * Sets the damage done to the vehicle
     *
     * @param damage The damage
     */
    void setDamage(double damage);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
