package org.bukkit.entity;

import org.bukkit.block.Dispenser;

/**
 * Represents a shootable entity
 */
public interface Projectile extends Entity {

    /**
     * Retrieve the shooter of this projectile. The returned value can be null
     * for projectiles shot from a {@link Dispenser} for example.
     *
     * @return the {@link LivingEntity} that shot this projectile
     */
    public LivingEntity getShooter();

    /**
     * Set the shooter of this projectile
     *
     * @param shooter the {@link LivingEntity} that shot this projectile
     */
    public void setShooter(LivingEntity shooter);

    /**
     * Determine if this projectile should bounce or not when it hits.
     * <p />
     * If a small fireball does not bounce it will set the target on fire.
     *
     * @return true if it should bounce.
     */
    public boolean doesBounce();

    /**
     * Set whether or not this projectile should bounce or not when it hits something.
     *
     * @param doesBounce whether or not it should bounce.
     */
    public void setBounce(boolean doesBounce);
}
