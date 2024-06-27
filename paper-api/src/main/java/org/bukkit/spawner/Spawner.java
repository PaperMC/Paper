package org.bukkit.spawner;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;

/**
 * Represents an entity spawner. <br>
 * May be a {@link SpawnerMinecart} or a {@link CreatureSpawner}.
 */
public interface Spawner extends BaseSpawner {

    /**
     * The minimum spawn delay amount (in ticks).
     * <br>
     * This value is used when the spawner resets its delay (for any reason).
     * It will choose a random number between {@link #getMinSpawnDelay()}
     * and {@link #getMaxSpawnDelay()} for its next {@link #getDelay()}.
     * <br>
     * Default value is 200 ticks.
     *
     * @return the minimum spawn delay amount
     */
    public int getMinSpawnDelay();

    /**
     * Set the minimum spawn delay amount (in ticks).
     *
     * @param delay the minimum spawn delay amount
     * @see #getMinSpawnDelay()
     */
    public void setMinSpawnDelay(int delay);

    /**
     * The maximum spawn delay amount (in ticks).
     * <br>
     * This value is used when the spawner resets its delay (for any reason).
     * It will choose a random number between {@link #getMinSpawnDelay()}
     * and {@link #getMaxSpawnDelay()} for its next {@link #getDelay()}.
     * <br>
     * This value <b>must</b> be greater than 0 and less than or equal to
     * {@link #getMaxSpawnDelay()}.
     * <br>
     * Default value is 800 ticks.
     *
     * @return the maximum spawn delay amount
     */
    public int getMaxSpawnDelay();

    /**
     * Set the maximum spawn delay amount (in ticks).
     * <br>
     * This value <b>must</b> be greater than 0, as well as greater than or
     * equal to {@link #getMinSpawnDelay()}
     *
     * @param delay the new maximum spawn delay amount
     * @see #getMaxSpawnDelay()
     */
    public void setMaxSpawnDelay(int delay);

    /**
     * Get how many mobs attempt to spawn.
     * <br>
     * Default value is 4.
     *
     * @return the current spawn count
     */
    public int getSpawnCount();

    /**
     * Set how many mobs attempt to spawn.
     *
     * @param spawnCount the new spawn count
     */
    public void setSpawnCount(int spawnCount);

    /**
     * Set the new maximum amount of similar entities that are allowed to be
     * within spawning range of this spawner.
     * <br>
     * If more than the maximum number of entities are within range, the spawner
     * will not spawn and try again with a new {@link #getDelay()}.
     * <br>
     * Default value is 16.
     *
     * @return the maximum number of nearby, similar, entities
     */
    public int getMaxNearbyEntities();

    /**
     * Set the maximum number of similar entities that are allowed to be within
     * spawning range of this spawner.
     * <br>
     * Similar entities are entities that are of the same {@link EntityType}
     *
     * @param maxNearbyEntities the maximum number of nearby, similar, entities
     */
    public void setMaxNearbyEntities(int maxNearbyEntities);
}
