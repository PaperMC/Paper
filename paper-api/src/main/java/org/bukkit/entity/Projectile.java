package org.bukkit.entity;

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
     * @param shooter
     *            the {@link LivingEntity} that shot this projectile
     */
    public void setShooter(LivingEntity shooter);
}
