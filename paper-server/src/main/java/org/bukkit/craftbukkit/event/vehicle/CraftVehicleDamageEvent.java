package org.bukkit.craftbukkit.event.vehicle;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.jspecify.annotations.Nullable;

public class CraftVehicleDamageEvent extends CraftVehicleEvent implements VehicleDamageEvent {

    private final DamageSource damageSource;
    private final Entity attacker;
    private double damage;

    private boolean cancelled;

    public CraftVehicleDamageEvent(final Vehicle vehicle, final DamageSource damageSource, final @Nullable Entity attacker, final double damage) {
        super(vehicle);
        this.damageSource = damageSource;
        this.attacker = attacker;
        this.damage = damage;
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
    public double getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(final double damage) {
        this.damage = damage;
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
        return VehicleDamageEvent.getHandlerList();
    }
}
