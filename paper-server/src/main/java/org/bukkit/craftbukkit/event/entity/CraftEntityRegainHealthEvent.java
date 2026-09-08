package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class CraftEntityRegainHealthEvent extends CraftEntityEvent implements EntityRegainHealthEvent {

    private double amount;
    private final RegainReason regainReason;
    private final boolean fastRegen;

    private boolean cancelled;

    public CraftEntityRegainHealthEvent(final Entity entity, final double amount, final RegainReason regainReason, final boolean fastRegen) {
        super(entity);
        this.amount = amount;
        this.regainReason = regainReason;
        this.fastRegen = fastRegen;
    }

    public CraftEntityRegainHealthEvent(final net.minecraft.world.entity.Entity entity, final double amount, final RegainReason regainReason, final boolean fastRegen) {
        this(entity.getBukkitEntity(), amount, regainReason, fastRegen);
    }

    @Override
    public double getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    @Override
    public RegainReason getRegainReason() {
        return this.regainReason;
    }

    @Override
    public boolean isFastRegen() {
        return this.fastRegen;
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
        return EntityRegainHealthEvent.getHandlerList();
    }
}
