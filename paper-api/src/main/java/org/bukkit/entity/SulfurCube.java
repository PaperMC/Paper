package org.bukkit.entity;

import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.entity.Shearable;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a Sulfur Cube.
 */
@NullMarked
public interface SulfurCube extends AbstractCubeMob, Shearable, Bucketable, Ageable {

    /**
     * Gets the amount of ticks until this sulfur cube explode.
     *
     * @return the fuse ticks or -1 if not exploding
     */
    int getFuseTicks();

    /**
     * Sets the amount of ticks until this sulfur cube explode.
     *
     * @param ticks the new fuse ticks
     */
    void setFuseTicks(int ticks);

    /**
     * Determines whether this sulfur cube is capable of exploding.
     *
     * @return {@code true} if the sulfur cube can explode, {@code false} otherwise
     */
    boolean canExplode();

    /**
     * Ignites this sulfur cube, beginning its fuse if {@link #canExplode()} is {@code true}.
     * <p>
     * The amount of time the sulfur cube takes to explode is defined in the {@link Archetype}
     * of the entity and further controlled by the {@code imminent} parameter.
     * <p>
     * This action can be cancelled using {@link io.papermc.paper.event.entity.EntityIgniteEvent}.
     * The resulting explosion can also be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @param imminent if {@code true} the fuse time is shortened but still depends on the {@link Archetype}
     * @return whether the sulfur cube got ignited
     * @see #canExplode()
     * @see #ignite()
     */
    boolean ignite(boolean imminent);

    /**
     * Ignites this sulfur cube, beginning its fuse if {@link #canExplode()} is {@code true}.
     * <p>
     * The amount of time the sulfur cube takes to explode is defined in the {@link Archetype}
     * of the entity.
     * <p>
     * This action can be cancelled using {@link io.papermc.paper.event.entity.EntityIgniteEvent}.
     * The resulting explosion can also be cancelled by an
     * {@link org.bukkit.event.entity.ExplosionPrimeEvent} and obeys the mob
     * griefing gamerule.
     *
     * @return whether the sulfur cube got ignited
     * @see #canExplode()
     * @see #ignite(boolean)
     */
    default boolean ignite() {
        return this.ignite(false);
    }

    /**
     * Equips the provided item to this sulfur cube, following any Vanilla logic.
     * <p>
     * This method will:
     * <ul>
     *     <li>not equip the item to a baby sulfur cube,</li>
     *     <li>drop an existing body item before, and</li>
     *     <li>play the absorb-sound.</li>
     * </ul>
     * <p>
     * If you want to circumvent the above-mentioned Vanilla logic, you can instead directly edit
     * the body equipment slot of the sulfur cube, like this:
     * <pre>{@code
     * sulfurCube.getEquipment().setItem(
     *   EquipmentSlot.BODY,
     *   itemStack
     * );
     * }</pre>
     *
     * @param itemStack the item to equip. Use {@link ItemStack#empty()} to unset the item
     * @return whether the sulfur cube's absorbed item was updated
     */
    boolean equipItem(ItemStack itemStack);

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
