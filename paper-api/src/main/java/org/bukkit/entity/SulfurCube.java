package org.bukkit.entity;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import io.papermc.paper.entity.Bucketable;
import io.papermc.paper.entity.Shearable;
import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.bukkit.inventory.EquipmentSlot;
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
     * Makes this sulfur cube swallow the provided item, following any Vanilla logic.
     * <p>
     * This method will:
     * <ul>
     *     <li>not equip the item to a baby sulfur cube,</li>
     *     <li>if present, drop a previously swallowed item, and</li>
     *     <li>play the swallow sound.</li>
     * </ul>
     * <p>
     * If the currently swallowed item is changed, a {@link EntityEquipmentChangedEvent} is called.
     * May also call a {@link EntityAddToWorldEvent} for the newly dropped {@link Item} entity.
     *
     * @param itemStack the item to swallow. Use {@link ItemStack#empty()} to unset the item.
     *                  Items not in the {@link ItemTypeTagKeys#SULFUR_CUBE_SWALLOWABLE} tag
     *                  will not be properly rendered inside the sulfur cube
     * @return whether the sulfur cube's absorbed item was updated
     * @see #setEquipped(ItemStack) set the swallowed item, skipping any Vanilla swallow logic
     */
    boolean swallow(ItemStack itemStack);

    /**
     * Sets the swallowed item stack for this sulfur cube.
     * <p>
     * This method acts as a simple utility method to set the {@link EquipmentSlot#BODY}
     * equipment slot of this sulfur cube, which holds the sulfur cube's swallowed item.
     * <p>
     * Different to {@link #swallow(ItemStack)}, this method does not play a sound or
     * drop the previously equipped item on the ground.
     * <p>
     * This method will call a {@link EntityEquipmentChangedEvent}.
     *
     * @param itemStack the item stack to be equipped
     * @see #swallow(ItemStack) set the swallowed item, following any Vanilla swallow logic
     */
    default void setEquipped(ItemStack itemStack) {
        this.getEquipment().setItem(EquipmentSlot.BODY, itemStack);
    }

    /**
     * Retrieves the item stack currently swallowed by this sulfur cube.
     * <p>
     * This method acts as a simple utility method to get the {@link EquipmentSlot#BODY}
     * equipment slot of this sulfur cube, which holds the sulfur cube's swallowed item.
     *
     * @return the item stack in the {@link EquipmentSlot#BODY} equipment slot
     */
    default ItemStack getEquipped() {
        return this.getEquipment().getItem(EquipmentSlot.BODY);
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
