package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;

public interface ShulkerBullet extends Entity {

    /**
     * Retrieve the shooter of this bullet.
     *
     * @return the {@link ProjectileSource} that shot this bullet
     */
    ProjectileSource getShooter();

    /**
     * Set the shooter of this bullet.
     *
     * @param source the {@link ProjectileSource} that shot this bullet
     */
    void setShooter(ProjectileSource source);

    /**
     * Retrieve the target of this bullet.
     *
     * @return the targeted entity
     */
    Entity getTarget();

    /**
     * Sets the target of this bullet
     *
     * @param target the entity to target
     */
    void setTarget(Entity target);
}
