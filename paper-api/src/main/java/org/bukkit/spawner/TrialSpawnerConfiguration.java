package org.bukkit.spawner;

import java.util.Map;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents one of the configurations of a trial spawner.
 */
@NullMarked
@ApiStatus.Experimental
public interface TrialSpawnerConfiguration extends BaseSpawner {

    /**
     * Gets the base number of entities the spawner will spawn before going into
     * cooldown.
     *
     * @return the number of entities
     */
    float getBaseSpawnsBeforeCooldown();

    /**
     * Sets the base number of entities the spawner will spawn before going into
     * cooldown.
     *
     * @param amount the number of entities
     */
    void setBaseSpawnsBeforeCooldown(float amount);

    /**
     * Gets the base number of entities this spawner can track at once. <br>
     * If the limit is reached the spawner will not be able to spawn any more
     * entities until the existing entities are killed or move too far away.
     *
     * @return the number of entities
     */
    float getBaseSimultaneousEntities();

    /**
     * Sets the base number of entities this spawner can track at once. <br>
     * If the limit is reached the spawner will not be able to spawn any more
     * entities until the existing entities are killed or move too far away.
     *
     * @param amount the number of entities
     */
    void setBaseSimultaneousEntities(float amount);

    /**
     * Gets the additional number of entities the spawner will spawn per tracked player
     * before going into cooldown.
     *
     * @return the number of entities
     */
    float getAdditionalSpawnsBeforeCooldown();

    /**
     * Sets the additional number of entities the spawner will spawn per tracked player
     * before going into cooldown.
     *
     * @param amount the number of entities
     */
    void setAdditionalSpawnsBeforeCooldown(float amount);

    /**
     * Gets the additional number of entities this spawner can track at once per
     * tracked player. <br>
     * If the limit is reached the spawner will not be able to spawn any more
     * entities until the existing entities are killed or move too far away.
     *
     * @return the number of entities
     */
    float getAdditionalSimultaneousEntities();

    /**
     * Sets the additional number of entities this spawner can track at once per
     * tracked player. <br>
     * If the limit is reached the spawner will not be able to spawn any more
     * entities until the existing entities are killed or move too far away.
     *
     * @param amount the number of entities
     */
    void setAdditionalSimultaneousEntities(float amount);

    /**
     * Gets a list of {@link LootTable}s this spawner can pick a reward from as
     * well as their associated weight to be chosen.
     *
     * @return a map of loot tables and their associated weight, or an empty
     *     map if there are none
     */
    Map<LootTable, Integer> getPossibleRewards();

    /**
     * Add a {@link LootTable} to the list of tables this spawner can pick a reward
     * from with a given weight.
     *
     * @param table the loot table
     * @param weight the weight, must be at least 1
     */
    void addPossibleReward(LootTable table, int weight);

    /**
     * Removes the provided {@link LootTable} from the list of tables this spawner
     * can pick a reward from.
     *
     * @param table the loot table
     */
    void removePossibleReward(LootTable table);

    /**
     * Sets the list of {@link LootTable}s and their weights this spawner can pick a
     * reward from. <br>
     * All loot tables in the map must be non-null and all weights must be at least
     * 1.
     *
     * @param rewards a map of loot tables and their weights, or null to clear all
     *     possible tables
     */
    void setPossibleRewards(Map<LootTable, Integer> rewards);
}
