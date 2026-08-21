package org.bukkit.craftbukkit.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.util.Vector;

@Deprecated(forRemoval = true)
public class CraftEntityKnockbackEvent extends CraftEntityEvent implements EntityKnockbackEvent {

    private final KnockbackCause cause;
    private final double force;
    private final Vector rawKnockback;
    private Vector knockback;

    private boolean cancelled;

    public CraftEntityKnockbackEvent(final LivingEntity entity, final KnockbackCause cause, final double force, final Vector rawKnockback, final Vector knockback) {
        super(entity);

        this.cause = cause;
        this.force = force;
        this.rawKnockback = rawKnockback;
        this.knockback = knockback;
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }

    @Override
    public KnockbackCause getCause() {
        return this.cause;
    }

    @Override
    public double getForce() {
        return this.force;
    }

    @Override
    public Vector getKnockback() {
        return this.rawKnockback.clone();
    }

    @Override
    public Vector getFinalKnockback() {
        return this.knockback.clone();
    }

    @Override
    public void setFinalKnockback(final Vector knockback) {
        Preconditions.checkArgument(knockback != null, "Knockback cannot be null");

        this.knockback = knockback.clone();
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
        return EntityKnockbackEvent.getHandlerList();
    }
}
