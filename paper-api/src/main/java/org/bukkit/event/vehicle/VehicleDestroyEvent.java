package org.bukkit.event.vehicle;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Raised when a vehicle is destroyed, which could be caused by either a
 * player or the environment. This is not raised if the boat is simply
 * 'removed' due to other means.
 */
public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final DamageSource damageSource;
    private final Entity attacker;
    private boolean cancelled;

    @ApiStatus.Internal
    public VehicleDestroyEvent(final @NotNull Vehicle vehicle, final @NotNull DamageSource damageSource, final @Nullable Entity attacker) {
        super(vehicle);
        this.damageSource = damageSource;
        this.attacker = attacker;
    }

    /**
     * Gets the DamageSource that has destroyed the vehicle.
     *
     * @return the DamageSource that has destroyed the vehicle
     */
    @NotNull
    public DamageSource getDamageSource() {
        return this.damageSource;
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
