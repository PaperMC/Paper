package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a shootable entity.
 */
public interface Projectile extends Entity {

    /**
     * Retrieve the shooter of this projectile.
     *
     * @return the {@link ProjectileSource} that shot this projectile
     */
    @Nullable
    public ProjectileSource getShooter();

    /**
     * Set the shooter of this projectile.
     *
     * @param source the {@link ProjectileSource} that shot this projectile
     */
    public void setShooter(@Nullable ProjectileSource source);

    /**
     * Determine if this projectile should bounce or not when it hits.
     *
     * @return true if it should bounce.
     * @deprecated does not do anything
     */
    @Deprecated
    public boolean doesBounce();

    /**
     * Set whether or not this projectile should bounce or not when it hits
     * something.
     *
     * @param doesBounce whether or not it should bounce.
     * @deprecated does not do anything
     */
    @Deprecated
    public void setBounce(boolean doesBounce);
}
