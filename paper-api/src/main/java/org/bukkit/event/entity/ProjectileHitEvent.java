package org.bukkit.event.entity;

import org.bukkit.entity.Projectile;

public class ProjectileHitEvent extends EntityEvent {

    public ProjectileHitEvent(Projectile projectile) {
        super(Type.PROJECTILE_HIT, projectile);
    }

}
