package org.bukkit.entity;

import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.entity.Shearable;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a Sulfur Cube.
 */
@NullMarked
public interface SulfurCube extends AbstractCubeMob, Shearable, Bucketable, Ageable {

    /**
     * Set the fuse ticks for this SulfurCube, where the ticks is the amount of
     * time in which a SulfurCube has been in the primed state.
     *
     * @param ticks the new fuse ticks
     */
    void setFuseTicks(int ticks);

    /**
     * Get the maximum fuse ticks for this SulfurCube, where the ticks is the
     * amount of time in which a SulfurCube has been in the primed state.
     *
     * @return the fuse ticks
     */
    int getFuseTicks();

    /**
     * Determines whether this SulfurCube is capable of exploding.
     *
     * @return true if the SulfurCube can explode, false otherwise
     */
    boolean canExplode();

    /**
     * Ignites this SulfurCube, beginning its fuse if {@link #canExplode()} is {@code true}.
     * <br>
     * The amount of time the SulfurCube takes to explode will depend on what
     * the fuse in the {@link Archetype} is set as.
     * <br>
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @param imminent if {@code true} the fuse used is a random value based in the fuse, otherwise the fuse is set from the archetype
     *
     * @see #canExplode()
     */
    void ignite(boolean imminent);

    /**
     * Ignites this SulfurCube, beginning its fuse if {@link #canExplode()} is {@code true}.
     * <br>
     * The amount of time the SulfurCube takes to explode will be a random value and
     * depend on what the fuse in the {@link Archetype} is set as.
     * <br>
     * The resulting explosion can be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @see #canExplode()
     */
    default void ignite() {
        this.ignite(false);
    }

    /**
     * Represents the archetype of a sulfur cube
     * which define a lot of possible behavior and interaction
     * throughout its lifetime.
     */
    interface Archetype extends Keyed {

        // Start generate - SulfurCubeArchetype
        Archetype BOUNCY = getArchetype("bouncy");

        Archetype EXPLOSIVE = getArchetype("explosive");

        Archetype FAST_FLAT = getArchetype("fast_flat");

        Archetype FAST_SLIDING = getArchetype("fast_sliding");

        Archetype HIGH_RESISTANCE = getArchetype("high_resistance");

        Archetype HOT = getArchetype("hot");

        Archetype LIGHT = getArchetype("light");

        Archetype REGULAR = getArchetype("regular");

        Archetype SLOW_BOUNCY = getArchetype("slow_bouncy");

        Archetype SLOW_FLAT = getArchetype("slow_flat");

        Archetype SLOW_SLIDING = getArchetype("slow_sliding");

        Archetype STICKY = getArchetype("sticky");
        // End generate - SulfurCubeArchetype

        private static Archetype getArchetype(@KeyPattern.Value final String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.SULFUR_CUBE_ARCHETYPE).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
        }
    }
}
