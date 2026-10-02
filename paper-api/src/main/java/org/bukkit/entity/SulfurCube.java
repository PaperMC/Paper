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

/// Represents a Sulfur Cube.
@NullMarked
public interface SulfurCube extends AbstractCubeMob, Shearable, Bucketable, Ageable {

    /// Gets the amount of ticks until this sulfur cube explode.
    ///
    /// @return the fuse ticks or -1 if not exploding
    int getFuseTicks();

    /// Sets the amount of ticks until this sulfur cube explode.
    ///
    /// @param ticks the new fuse ticks
    void setFuseTicks(int ticks);

    /// Determines whether this sulfur cube is capable of exploding.
    ///
    /// @return `true` if the sulfur cube can explode, `false` otherwise
    boolean canExplode();

    /// Ignites this sulfur cube, beginning its fuse if [#canExplode()] is `true`.
    ///
    /// The amount of time the sulfur cube takes to explode is defined in the [Archetype]
    /// of the entity and further controlled by the `imminent` parameter.
    ///
    /// This action can be cancelled using [io.papermc.paper.event.entity.EntityIgniteEvent].
    /// The resulting explosion can also be cancelled by an
    /// [org.bukkit.event.entity.ExplosionPrimeEvent] and obeys the mob
    /// griefing gamerule.
    ///
    /// @param imminent if `true` the fuse time is shortened but still depends on the [Archetype]
    /// @return whether the sulfur cube got ignited
    /// @see #canExplode()
    /// @see #ignite()
    boolean ignite(boolean imminent);

    /// Ignites this sulfur cube, beginning its fuse if [#canExplode()] is `true`.
    ///
    /// The amount of time the sulfur cube takes to explode is defined in the [Archetype]
    /// of the entity.
    ///
    /// This action can be cancelled using [io.papermc.paper.event.entity.EntityIgniteEvent].
    /// The resulting explosion can also be cancelled by an
    /// [org.bukkit.event.entity.ExplosionPrimeEvent] and obeys the mob
    /// griefing gamerule.
    ///
    /// @return whether the sulfur cube got ignited
    /// @see #canExplode()
    /// @see #ignite(boolean)
    default boolean ignite() {
        return this.ignite(false);
    }

    /// Makes this sulfur cube swallow the provided item, following any Vanilla logic.
    ///
    /// This method will:
    ///
    ///   - not equip the item to a baby sulfur cube,
    ///   - if present, drop a previously swallowed item, and
    ///   - play the swallow sound.
    ///
    /// If the currently swallowed item is changed, a [EntityEquipmentChangedEvent] is called.
    /// May also call a [EntityAddToWorldEvent] for the newly dropped [Item] entity.
    ///
    /// @param itemStack the item to swallow. Use [ItemStack#empty()] to unset the item.
    ///                  Items not in the [ItemTypeTagKeys#SULFUR_CUBE_SWALLOWABLE] tag
    ///                  will not be properly rendered inside the sulfur cube
    /// @return whether the sulfur cube's absorbed item was updated
    /// @see #setEquipped(ItemStack) set the swallowed item, skipping any Vanilla swallow logic
    boolean swallow(ItemStack itemStack);

    /// Sets the swallowed item stack for this sulfur cube.
    ///
    /// This method acts as a simple utility method to set the [EquipmentSlot#BODY]
    /// equipment slot of this sulfur cube, which holds the sulfur cube's swallowed item.
    ///
    /// Different to [#swallow(ItemStack)], this method does not play a sound or
    /// drop the previously equipped item on the ground.
    ///
    /// This method will call a [EntityEquipmentChangedEvent].
    ///
    /// @param itemStack the item stack to be equipped, use [ItemStack#empty()] to unset the item
    /// @see #swallow(ItemStack) set the swallowed item, following any Vanilla swallow logic
    default void setEquipped(final ItemStack itemStack) {
        this.getEquipment().setItem(EquipmentSlot.BODY, itemStack);
    }

    /// Retrieves the item stack currently swallowed by this sulfur cube.
    ///
    /// This method acts as a simple utility method to get the [EquipmentSlot#BODY]
    /// equipment slot of this sulfur cube, which holds the sulfur cube's swallowed item.
    ///
    /// @return the item stack in the [EquipmentSlot#BODY] equipment slot, returns [ItemStack#empty()]
    /// if no item is present
    default ItemStack getEquipped() {
        return this.getEquipment().getItem(EquipmentSlot.BODY);
    }

    /// Represents the archetype of a sulfur cube
    /// which define a lot of possible behavior and interaction
    /// throughout its lifetime.
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
