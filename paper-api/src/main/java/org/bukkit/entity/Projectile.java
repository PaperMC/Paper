package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;

/**
 * Represents a shootable entity.
 */
public interface Projectile extends Entity {

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @return the {@link LivingEntity} that shot this projectile
     */
    @Deprecated
    public LivingEntity _INVALID_getShooter();

    /**
     * Retrieve the shooter of this projectile.
     *
     * @return the {@link ProjectileSource} that shot this projectile
     */
    public ProjectileSource getShooter();

    /**
     * This method exists for legacy reasons to provide backwards
     * compatibility. It will not exist at runtime and should not be used
     * under any circumstances.
     *
     * @param shooter the {@link LivingEntity} that shot this projectile
     */
    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter);

    /**
     * Set the shooter of this projectile.
     *
     * @param source the {@link ProjectileSource} that shot this projectile
     */
    public void setShooter(ProjectileSource source);

    /**
     * Determine if this projectile should bounce or not when it hits.
     * <p>
     * If a small fireball does not bounce it will set the target on fire.
     *
     * @return true if it should bounce.
     */
    public boolean doesBounce();

    /**
     * Set whether or not this projectile should bounce or not when it hits
     * something.
     *
     * @param doesBounce whether or not it should bounce.
     */
    public void setBounce(boolean doesBounce);
}
