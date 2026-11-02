package org.bukkit.projectiles;

import java.util.function.Consumer;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import org.jspecify.annotations.Nullable;

/**
 * Represents a valid source of a projectile.
 */
public interface ProjectileSource {

    /**
     * Launches a {@link Projectile} from the ProjectileSource.
     * <p>
     * The family of launchProjectile methods only promise the ability to launch projectile types
     * that the ProjectileSource is capable of firing in vanilla.
     * Any other types of projectiles *may* be implemented but are not part of the method contract.
     *
     * @param projectile class of the projectile to launch
     * @param <T>        a projectile subclass
     * @return the launched projectile
     */
    default <T extends Projectile> T launchProjectile(final Class<? extends T> projectile) {
        return this.launchProjectile(projectile, null);
    }

    /**
     * Launches a {@link Projectile} from the ProjectileSource with an
     * initial velocity.
     * <p>
     * The family of launchProjectile methods only promise the ability to launch projectile types
     * that the ProjectileSource is capable of firing in vanilla.
     * Any other types of projectiles *may* be implemented but are not part of the method contract.
     *
     * @param projectile class of the projectile to launch
     * @param velocity   the velocity with which to launch
     * @param <T>        a projectile subclass
     * @return the launched projectile
     */
    default <T extends Projectile> T launchProjectile(final Class<? extends T> projectile, final @Nullable Vector velocity) {
        return this.launchProjectile(projectile, velocity, null);
    }

    // Paper start - add consumer to launchProjectile

    /**
     * Launches a {@link Projectile} from the ProjectileSource with an
     * initial velocity, with the supplied function run before the
     * entity is added to the world.
     * <br>
     * Note that when the function is run, the entity will not be actually in
     * the world. Any operation involving such as teleporting the entity is undefined
     * until after this function returns.
     * <p>
     * The family of launchProjectile methods only promise the ability to launch projectile types
     * that the ProjectileSource is capable of firing in vanilla.
     * Any other types of projectiles *may* be implemented but are not part of the method contract.
     *
     * @param projectile class of the projectile to launch
     * @param velocity   the velocity with which to launch
     * @param function   the function to be run before the entity is spawned
     * @param <T>        a projectile subclass
     * @return the launched projectile
     */
    <T extends Projectile> T launchProjectile(Class<? extends T> projectile, @Nullable Vector velocity, @Nullable Consumer<? super T> function);
    // Paper end - add consumer to launchProjectile
}
