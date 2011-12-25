package org.bukkit.event.entity;

import org.bukkit.entity.Projectile;

/**
 * Called when a projectile hits an object
 */
@SuppressWarnings("serial")
public class ProjectileHitEvent extends EntityEvent {

    public ProjectileHitEvent(Projectile projectile) {
        super(Type.PROJECTILE_HIT, projectile);
    }

}
