package org.bukkit.entity;

import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.entity.Shearable;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;

/**
 * Represents a Sulfur Cube.
 */
public interface SulfurCube extends AbstractCubeMob, Shearable, Bucketable, Ageable {

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
