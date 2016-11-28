package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.HandlerList;

/**
 * Called when a projectile hits an object
 */
public class ProjectileHitEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Entity hitEntity;

    public ProjectileHitEvent(final Projectile projectile) {
        this(projectile, null);
    }

    public ProjectileHitEvent(final Projectile projectile, Entity hitEntity) {
        super(projectile);
        this.hitEntity = hitEntity;
    }

    @Override
    public Projectile getEntity() {
        return (Projectile) entity;
    }

    /**
     * Gets the entity that was hit, if it was an entity that was hit.
     *
     * @return hit entity or else null
     */
    public Entity getHitEntity() {
        return hitEntity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
