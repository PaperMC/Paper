package org.bukkit.projectiles;

import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a valid source of a projectile.
 */
public interface ProjectileSource {

    /**
     * Launches a {@link Projectile} from the ProjectileSource.
     * <p>
     * The family of launchProjectile methods only promise the ability to launch projectile types
     * that the {@link ProjectileSource} is capable of firing in vanilla.
     * Any other types of projectiles *may* be implemented but are not part of the method contract.
     *
     * @param <T> a projectile subclass
     * @param projectile class of the projectile to launch
     * @return the launched projectile
     */
    @NotNull
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile);

    /**
     * Launches a {@link Projectile} from the ProjectileSource with an
     * initial velocity.
     * <p>
     * The family of launchProjectile methods only promise the ability to launch projectile types
     * that the {@link ProjectileSource} is capable of firing in vanilla.
     * Any other types of projectiles *may* be implemented but are not part of the method contract.
     *
     * @param <T> a projectile subclass
     * @param projectile class of the projectile to launch
     * @param velocity the velocity with which to launch
     * @return the launched projectile
     */
    @NotNull
    public <T extends Projectile> T launchProjectile(@NotNull Class<? extends T> projectile, @Nullable Vector velocity);
}
