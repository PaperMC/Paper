package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

/**
 * Called when an entity is damaged by a projectile
 *
 * @deprecated use {@link EntityDamageByEntityEvent} instead, where {@link EntityDamageByEntityEvent#getDamager()} will return the {@link Projectile}
 */
@Deprecated
public class EntityDamageByProjectileEvent extends EntityDamageByEntityEvent {

    private Projectile projectile;

    public EntityDamageByProjectileEvent(Entity damagee, Projectile projectile, DamageCause cause, int damage) {
        this(projectile.getShooter(), damagee, projectile, cause, damage);
    }

    public EntityDamageByProjectileEvent(Entity damager, Entity damagee, Projectile projectile, DamageCause cause, int damage) {
        super(damager, projectile, DamageCause.PROJECTILE, damage);
        this.projectile = projectile;
    }

    /**
     * The projectile used to cause the event
     *
     * @return the projectile
     */
    public Projectile getProjectile() {
        return projectile;
    }

    public void setBounce(boolean bounce) {
        projectile.setBounce(bounce);
    }

    public boolean getBounce() {
        return projectile.doesBounce();
    }
}
