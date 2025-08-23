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

/**
 * Represents a basic entity spawner. <br>
 * May be a {@link SpawnerMinecart}, {@link CreatureSpawner} or {@link TrialSpawnerConfiguration}.
 */
@NullMarked
public interface BaseSpawner {

    /**
     * Get the spawner's creature type.
     *
     * @return The creature type or null if it not set.
     */
    @Nullable
    EntityType getSpawnedType();

    /**
     * Set the spawner's creature type. <br>
     * This will override any entities that have been added with {@link #addPotentialSpawn}
     *
     * @param creatureType The creature type or null to clear.
     */
    void setSpawnedType(@Nullable EntityType creatureType);

    /**
     * Get the spawner's delay.
     * <br>
     * This is the delay, in ticks, until the spawner will spawn its next mob.
     *
     * @return The delay.
     */
    int getDelay();

    /**
     * Set the spawner's delay.
     *
     * @param delay The delay.
     */
    void setDelay(int delay);

    /**
     * Get the maximum distance(squared) a player can be in order for this
     * spawner to be active.
     * <br>
     * If this value is less than or equal to 0, this spawner is always active
     * (given that there are players online).
     * <br>
     * Default value is 16.
     *
     * @return the maximum distance(squared) a player can be in order for this
     * spawner to be active.
     */
    int getRequiredPlayerRange();

    /**
     * Set the maximum distance (squared) a player can be in order for this
     * spawner to be active.
     * <br>
     * Setting this value to less than or equal to 0 will make this spawner
     * always active (given that there are players online).
     *
     * @param requiredPlayerRange the maximum distance (squared) a player can be
     * in order for this spawner to be active.
     */
    void setRequiredPlayerRange(int requiredPlayerRange);

    /**
     * Get the radius around which the spawner will attempt to spawn mobs in.
     * <br>
     * This area is square, includes the block the spawner is in, and is
     * centered on the spawner's x,z coordinates - not the spawner itself.
     * <br>
     * It is 2 blocks high, centered on the spawner's y-coordinate (its bottom);
     * thus allowing mobs to spawn as high as its top surface and as low
     * as 1 block below its bottom surface.
     * <br>
     * Default value is 4.
     *
     * @return the spawn range
     */
    int getSpawnRange();

    /**
     * Set the new spawn range.
     * <br>
     *
     * @param spawnRange the new spawn range
     * @see #getSpawnRange()
     */
    void setSpawnRange(int spawnRange);

    /**
     * Gets the {@link EntitySnapshot} that will be spawned by this spawner or null
     * if no entities have been assigned to this spawner. <br>
     * <p>
     * All applicable data from the spawner will be copied, such as custom name,
     * health, and velocity. <br>
     *
     * @return the entity snapshot or null if no entities have been assigned to this
     *         spawner.
     */
    @Nullable
    EntitySnapshot getSpawnedEntity();

    /**
     * Sets the entity that will be spawned by this spawner. <br>
     * This will override any previous entries that have been added with
     * {@link #addPotentialSpawn}
     * <p>
     * All applicable data from the snapshot will be copied, such as custom name,
     * health, and velocity. <br>
     *
     * @param snapshot the entity snapshot or null to clear
     */
    void setSpawnedEntity(@Nullable EntitySnapshot snapshot);

    /**
     * Sets the {@link SpawnerEntry} that will be spawned by this spawner. <br>
     * This will override any previous entries that have been added with
     * {@link #addPotentialSpawn}
     *
     * @param spawnerEntry the spawner entry to use
     */
    void setSpawnedEntity(SpawnerEntry spawnerEntry);

    /**
     * Adds a new {@link EntitySnapshot} to the list of entities this spawner can
     * spawn.
     * <p>
     * The weight will determine how often this entry is chosen to spawn, higher
     * weighted entries will spawn more often than lower-weighted ones. <br>
     * The {@link SpawnRule} will determine under what conditions this entry can
     * spawn, passing null will use the default conditions for the given entity.
     *
     * @param snapshot  the snapshot that will be spawned
     * @param weight    the weight
     * @param spawnRule the spawn rule for this entity, or null
     */
    void addPotentialSpawn(EntitySnapshot snapshot, int weight, @Nullable SpawnRule spawnRule);

    /**
     * Adds a new {@link SpawnerEntry} to the list of entities this spawner can
     * spawn.
     *
     * @param spawnerEntry the spawner entry to use
     * @see #addPotentialSpawn(EntitySnapshot, int, SpawnRule)
     */
    void addPotentialSpawn(final SpawnerEntry spawnerEntry);

    /**
     * Sets the list of {@link SpawnerEntry} this spawner can spawn. <br>
     * This will override any previous entries added with
     * {@link #addPotentialSpawn}
     *
     * @param entries the list of entries
     */
    void setPotentialSpawns(final Collection<SpawnerEntry> entries);

    /**
     * Gets a list of potential spawns from this spawner or an empty list if no
     * entities have been assigned to this spawner. <br>
     * Changes made to the returned list will not be reflected in the spawner unless
     * applied with {@link #setPotentialSpawns}
     *
     * @return a list of potential spawns from this spawner, or an empty list if no
     *         entities have been assigned to this spawner
     * @see #getSpawnedType()
     */
    List<SpawnerEntry> getPotentialSpawns();
}
