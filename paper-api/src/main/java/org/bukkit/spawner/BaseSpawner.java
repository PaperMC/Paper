package org.bukkit.spawner;

import java.util.Collection;
import java.util.List;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.spawner.SpawnRule;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/// Represents a basic entity spawner.
///
/// May be a [SpawnerMinecart], [CreatureSpawner] or [TrialSpawnerConfiguration].
@NullMarked
public interface BaseSpawner {

    /// Get the spawner's creature type.
    ///
    /// @return The creature type or null if it not set.
    @Nullable
    EntityType getSpawnedType();

    /// Set the spawner's creature type.
    ///
    /// This will override any entities that have been added with [#addPotentialSpawn]
    ///
    /// @param creatureType The creature type or null to clear.
    void setSpawnedType(@Nullable EntityType creatureType);

    /// Get the spawner's delay.
    ///
    /// This is the delay, in ticks, until the spawner will spawn its next mob.
    ///
    /// @return The delay.
    int getDelay();

    /// Set the spawner's delay.
    ///
    /// @param delay The delay.
    void setDelay(int delay);

    /// Get the maximum distance(squared) a player can be in order for this
    /// spawner to be active.
    ///
    /// If this value is less than or equal to 0, this spawner is always active
    /// (given that there are players online).
    ///
    /// Default value is 16.
    ///
    /// @return the maximum distance(squared) a player can be in order for this
    /// spawner to be active.
    int getRequiredPlayerRange();

    /// Set the maximum distance (squared) a player can be in order for this
    /// spawner to be active.
    ///
    /// Setting this value to less than or equal to 0 will make this spawner
    /// always active (given that there are players online).
    ///
    /// @param requiredPlayerRange the maximum distance (squared) a player can be
    /// in order for this spawner to be active.
    void setRequiredPlayerRange(int requiredPlayerRange);

    /// Get the radius around which the spawner will attempt to spawn mobs in.
    ///
    /// This area is square, includes the block the spawner is in, and is
    /// centered on the spawner's x,z coordinates - not the spawner itself.
    ///
    /// It is 2 blocks high, centered on the spawner's y-coordinate (its bottom);
    /// thus allowing mobs to spawn as high as its top surface and as low
    /// as 1 block below its bottom surface.
    ///
    /// Default value is 4.
    ///
    /// @return the spawn range
    int getSpawnRange();

    /// Set the new spawn range.
    ///
    /// @param spawnRange the new spawn range
    /// @see #getSpawnRange()
    void setSpawnRange(int spawnRange);

    /// Gets the [EntitySnapshot] that will be spawned by this spawner or null
    /// if no entities have been assigned to this spawner.
    ///
    /// All applicable data from the spawner will be copied, such as custom name,
    /// health, and velocity.
    ///
    /// @return the entity snapshot or null if no entities have been assigned to this
    ///         spawner.
    @Nullable
    EntitySnapshot getSpawnedEntity();

    /// Sets the entity that will be spawned by this spawner.
    ///
    /// This will override any previous entries that have been added with
    /// [#addPotentialSpawn]
    ///
    /// All applicable data from the snapshot will be copied, such as custom name,
    /// health, and velocity.
    ///
    /// @param snapshot the entity snapshot or null to clear
    void setSpawnedEntity(@Nullable EntitySnapshot snapshot);

    /// Sets the [SpawnerEntry] that will be spawned by this spawner.
    ///
    /// This will override any previous entries that have been added with
    /// [#addPotentialSpawn]
    ///
    /// @param spawnerEntry the spawner entry to use
    void setSpawnedEntity(SpawnerEntry spawnerEntry);

    /// Adds a new [EntitySnapshot] to the list of entities this spawner can
    /// spawn.
    ///
    /// The weight will determine how often this entry is chosen to spawn, higher
    /// weighted entries will spawn more often than lower-weighted ones.
    ///
    /// The [SpawnRule] will determine under what conditions this entry can
    /// spawn, passing null will use the default conditions for the given entity.
    ///
    /// @param snapshot  the snapshot that will be spawned
    /// @param weight    the weight
    /// @param spawnRule the spawn rule for this entity, or null
    void addPotentialSpawn(EntitySnapshot snapshot, int weight, @Nullable SpawnRule spawnRule);

    /// Adds a new [SpawnerEntry] to the list of entities this spawner can
    /// spawn.
    ///
    /// @param spawnerEntry the spawner entry to use
    /// @see #addPotentialSpawn(EntitySnapshot, int, SpawnRule)
    void addPotentialSpawn(final SpawnerEntry spawnerEntry);

    /// Sets the list of [SpawnerEntry] this spawner can spawn.
    ///
    /// This will override any previous entries added with
    /// [#addPotentialSpawn]
    ///
    /// @param entries the list of entries
    void setPotentialSpawns(final Collection<SpawnerEntry> entries);

    /// Gets a list of potential spawns from this spawner or an empty list if no
    /// entities have been assigned to this spawner.
    ///
    /// Changes made to the returned list will not be reflected in the spawner unless
    /// applied with [#setPotentialSpawns]
    ///
    /// @return a list of potential spawns from this spawner, or an empty list if no
    ///         entities have been assigned to this spawner
    /// @see #getSpawnedType()
    List<SpawnerEntry> getPotentialSpawns();
}
