package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.ProjectileCollideEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;

@Deprecated(since = "1.19.3")
public class PaperProjectileCollideEvent extends CraftEntityEvent implements ProjectileCollideEvent {

    private final Entity collidedWith;

    private boolean cancelled;

    public PaperProjectileCollideEvent(final Projectile projectile, final Entity collidedWith) {
        super(projectile);
        this.collidedWith = collidedWith;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) this.entity;
    }

    @Override
    public Entity getCollidedWith() {
        return this.collidedWith;
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
        return ProjectileCollideEvent.getHandlerList();
    }
}
