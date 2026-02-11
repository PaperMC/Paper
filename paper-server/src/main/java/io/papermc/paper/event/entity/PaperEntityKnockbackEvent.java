package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class PaperEntityKnockbackEvent extends CraftEntityEvent implements EntityKnockbackEvent {

    private final Cause cause;

    protected Vector knockback;
    private boolean cancelled;

    public PaperEntityKnockbackEvent(final Entity entity, final Cause cause, final Vector knockback) {
        super(entity);
        this.cause = cause;
        this.knockback = knockback;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public Vector getKnockback() {
        return this.knockback.clone();
    }

    @Override
    public void setKnockback(final Vector knockback) {
        Preconditions.checkArgument(knockback != null, "knockback cannot be null");
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

    public HandlerList getHandlers() {
        return EntityKnockbackEvent.getHandlerList();
    }
}
