package org.bukkit.block.spawner;

import com.google.common.base.Preconditions;
import org.bukkit.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a weighted spawn potential that can be added to a monster spawner.
 */
public class SpawnerEntry {

    private EntitySnapshot snapshot;
    private int spawnWeight;
    private SpawnRule spawnRule;

    public SpawnerEntry(@NotNull EntitySnapshot snapshot, int spawnWeight, @Nullable SpawnRule spawnRule) {
        Preconditions.checkArgument(snapshot != null, "Snapshot cannot be null");

        this.snapshot = snapshot;
        this.spawnWeight = spawnWeight;
        this.spawnRule = spawnRule;
    }

    /**
     * Gets the {@link EntitySnapshot} for this SpawnerEntry.
     *
     * @return the snapshot
     */
    @NotNull
    public EntitySnapshot getSnapshot() {
        return snapshot;
    }

    /**
     * Sets the {@link EntitySnapshot} for this SpawnerEntry.
     *
     * @param snapshot the snapshot
     */
    public void setSnapshot(@NotNull EntitySnapshot snapshot) {
        Preconditions.checkArgument(snapshot != null, "Snapshot cannot be null");
        this.snapshot = snapshot;
    }

    /**
     * Gets the weight for this SpawnerEntry, when added to a spawner entries
     * with higher weight will spawn more often.
     *
     * @return the weight
     */
    public int getSpawnWeight() {
        return spawnWeight;
    }

    /**
     * Sets the weight for this SpawnerEntry, when added to a spawner entries
     * with higher weight will spawn more often.
     *
     * @param spawnWeight the new spawn weight
     */
    public void setSpawnWeight(int spawnWeight) {
        this.spawnWeight = spawnWeight;
    }

    /**
     * Gets a copy of the {@link SpawnRule} for this SpawnerEntry, or null if
     * none has been set.
     *
     * @return a copy of the spawn rule or null
     */
    @Nullable
    public SpawnRule getSpawnRule() {
        return spawnRule == null ? null : spawnRule.clone();
    }

    /**
     * Sets the {@link SpawnRule} for this SpawnerEntry, null may be used to
     * clear the current spawn rule.
     *
     * @param spawnRule the new spawn rule to use or null
     */
    public void setSpawnRule(@Nullable SpawnRule spawnRule) {
        this.spawnRule = spawnRule;
    }
}
