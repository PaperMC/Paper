package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a projectile collides with an entity
 * <p>
 * This event is called <b>before</b> {@link EntityDamageByEntityEvent}, and cancelling it will allow the projectile to continue flying
 *
 * @deprecated Deprecated, use {@link org.bukkit.event.entity.ProjectileHitEvent} and check if there is a hit entity
 */
@Deprecated(since = "1.19.3")
public class ProjectileCollideEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NotNull private final Entity collidedWith;

    private boolean cancelled;

    @ApiStatus.Internal
    public ProjectileCollideEvent(@NotNull Projectile projectile, @NotNull Entity collidedWith) {
        super(projectile);
        this.collidedWith = collidedWith;
    }

    /**
     * Get the projectile that collided
     *
     * @return the projectile that collided
     */
    @NotNull
    public Projectile getEntity() {
        return (Projectile) super.getEntity();
    }

    /**
     * Get the entity the projectile collided with
     *
     * @return the entity collided with
     */
    @NotNull
    public Entity getCollidedWith() {
        return this.collidedWith;
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
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
