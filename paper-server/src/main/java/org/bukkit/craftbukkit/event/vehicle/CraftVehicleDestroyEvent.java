package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.jspecify.annotations.Nullable;

public class CraftVehicleDestroyEvent extends CraftVehicleEvent implements VehicleDestroyEvent {

    private final DamageSource damageSource;
    private final Entity attacker;
    private boolean cancelled;

    public CraftVehicleDestroyEvent(final Vehicle vehicle, final DamageSource damageSource, final @Nullable Entity attacker) {
        super(vehicle);
        this.damageSource = damageSource;
        this.attacker = attacker;
    }

    @Override
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public @Nullable Entity getAttacker() {
        return this.attacker;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return VehicleDestroyEvent.getHandlerList();
    }
}
