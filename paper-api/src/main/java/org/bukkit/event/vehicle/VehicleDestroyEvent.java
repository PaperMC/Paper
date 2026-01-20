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
    private boolean cancelled;

    @ApiStatus.Internal
    public VehicleDestroyEvent(@NotNull final Vehicle vehicle, final DamageSource damageSource) {
        super(vehicle);
        this.damageSource = damageSource;
    }

    /**
     * Gets the DamageSource that caused the damage.
     *
     * @return the DamageSource that caused the damage.
     */
    @NotNull
    public DamageSource getDamageSource() {
        return damageSource;
    }

    /**
     * Gets the Entity that has destroyed the vehicle, potentially null
     *
     * @return the Entity that has destroyed the vehicle, potentially null
     */
    @Nullable
    public Entity getAttacker() {
        return this.getDamageSource().getCausingEntity();
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
