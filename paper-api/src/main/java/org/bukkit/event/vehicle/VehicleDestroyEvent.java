package org.bukkit.event.vehicle;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Raised when a vehicle is destroyed, which could be caused by either a
 * player or the environment. This is not raised if the boat is simply
 * 'removed' due to other means.
 */
public interface VehicleDestroyEvent extends VehicleEventNew, Cancellable {

    /**
     * Gets the DamageSource that has destroyed the vehicle.
     *
     * @return the DamageSource that has destroyed the vehicle
     */
    DamageSource getDamageSource();

    /**
     * Gets the Entity that has destroyed the vehicle, potentially null
     *
     * @return the Entity that has destroyed the vehicle, potentially null
     */
    @Nullable Entity getAttacker();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
