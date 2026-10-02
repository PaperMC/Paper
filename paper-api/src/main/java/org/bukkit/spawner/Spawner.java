package org.bukkit.spawner;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

/// Represents an entity spawner.
///
/// May be a [SpawnerMinecart] or a [CreatureSpawner].
@NullMarked
public interface Spawner extends BaseSpawner {

    /// {@inheritDoc}
    ///
    /// If set to -1, the spawn delay will be reset to a random value between
    /// [#getMinSpawnDelay] and [#getMaxSpawnDelay()].
    ///
    /// @param delay The delay.
    @Override
    void setDelay(int delay);

    /// The minimum spawn delay amount (in ticks).
    ///
    /// This value is used when the spawner resets its delay (for any reason).
    /// It will choose a random number between [#getMinSpawnDelay()]
    /// and [#getMaxSpawnDelay()] for its next [#getDelay()].
    ///
    /// Default value is 200 ticks.
    ///
    /// @return the minimum spawn delay amount
    int getMinSpawnDelay();

    /// Set the minimum spawn delay amount (in ticks).
    ///
    /// @param delay the minimum spawn delay amount
    /// @see #getMinSpawnDelay()
    void setMinSpawnDelay(int delay);

    /// The maximum spawn delay amount (in ticks).
    ///
    /// This value is used when the spawner resets its delay (for any reason).
    /// It will choose a random number between [#getMinSpawnDelay()]
    /// and [#getMaxSpawnDelay()] for its next [#getDelay()].
    ///
    /// This value **must** be greater than 0 and less than or equal to
    /// [#getMaxSpawnDelay()].
    ///
    /// Default value is 800 ticks.
    ///
    /// @return the maximum spawn delay amount
    int getMaxSpawnDelay();

    /// Set the maximum spawn delay amount (in ticks).
    ///
    /// This value **must** be greater than 0, as well as greater than or
    /// equal to [#getMinSpawnDelay()]
    ///
    /// @param delay the new maximum spawn delay amount
    /// @see #getMaxSpawnDelay()
    void setMaxSpawnDelay(int delay);

    /// Get how many mobs attempt to spawn.
    ///
    /// Default value is 4.
    ///
    /// @return the current spawn count
    int getSpawnCount();

    /// Set how many mobs attempt to spawn.
    ///
    /// @param spawnCount the new spawn count
    void setSpawnCount(int spawnCount);

    /// Get the maximum number of similar entities that are allowed to be
    /// within the spawning range of this spawner.
    ///
    /// If more than the maximum number of entities are within range, the spawner
    /// will not spawn and try again with a new [#getDelay()].
    ///
    /// Default value is 6.
    ///
    /// @return the maximum number of nearby, similar, entities
    int getMaxNearbyEntities();

    /// Set the maximum number of similar entities that are allowed to be within
    /// spawning range of this spawner.
    ///
    /// Similar entities are entities that are of the same [EntityType]
    ///
    /// @param maxNearbyEntities the maximum number of nearby, similar, entities
    void setMaxNearbyEntities(int maxNearbyEntities);

    /// Check if spawner is activated (a player is close enough)
    ///
    /// @return True if a player is close enough to activate it
    boolean isActivated();

    /// Resets the spawn delay timer within the min/max range
    void resetTimer();

    /// Sets the [EntityType] to [EntityType#ITEM] and sets the data to the given
    /// [`ItemStack`][org.bukkit.inventory.ItemStack].
    ///
    /// [#setSpawnCount(int)] does not dictate the amount of items in the stack spawned, but rather how many
    /// stacks should be spawned.
    ///
    /// @param itemStack The item to spawn. Must not [`be empty`][ItemStack#isEmpty()].
    /// @see #setSpawnedType(EntityType)
    void setSpawnedItem(ItemStack itemStack);
}
