package com.destroystokyo.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;

/**
 * Called when a projectile collides with an entity
 * <p>
 * This event is called <b>before</b> {@link EntityDamageByEntityEvent}, and cancelling it will allow the projectile to continue flying
 *
 * @deprecated use {@link org.bukkit.event.entity.ProjectileHitEvent} and check if there is a hit entity
 */
@Deprecated(since = "1.19.3")
public interface ProjectileCollideEvent extends EntityEvent, Cancellable {

    /**
     * Get the projectile that collided
     *
     * @return the projectile that collided
     */
    @Override
    Projectile getEntity();

    /**
     * Get the entity the projectile collided with
     *
     * @return the entity collided with
     */
    Entity getCollidedWith();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
