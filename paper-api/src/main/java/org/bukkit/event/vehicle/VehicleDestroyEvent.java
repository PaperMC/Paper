package org.bukkit.event.vehicle;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

/**
 * Raised when a vehicle is destroyed, which could be caused by either a
 * player or the environment. This is not raised if the boat is simply
 * 'removed' due to other means.
 */
@NullMarked
public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Nullable
    private final Entity attacker;
    private boolean cancelled;

    private final DamageSource damageSource;

    @ApiStatus.Internal
    public VehicleDestroyEvent(Vehicle vehicle, @Nullable final Entity attacker, DamageSource damageSource) {
        super(vehicle);
        this.attacker = attacker;
        this.damageSource = damageSource;
    }

    /**
     * Gets the Entity that has destroyed the vehicle, potentially null
     *
     * @return the Entity that has destroyed the vehicle, potentially null
     */
    @Nullable
    public Entity getAttacker() {
        return this.attacker;
    }

    /**
     * Gets the DamageSource that caused this vehicle to be destroyed
     *
     * @return DamageSource that caused this vehicle to be destroyed
     */
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
