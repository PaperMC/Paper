package io.papermc.paper.registry.data;

import io.papermc.paper.block.TrialSpawnerConfig;
import io.papermc.paper.registry.RegistryBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import org.bukkit.block.spawner.SpawnerEntry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A data-centric version-specific registry entry for the {@link io.papermc.paper.block.TrialSpawnerConfig} type.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface TrialSpawnerConfigRegistryEntry {

    /**
     * Provides the radius around which this trial spawner config will attempt to spawn mobs in.
     * The covered area is a square.
     *
     * @return the radius (in blocks)
     * @see TrialSpawnerConfig#getSpawnRange()
     */
    @IntRange(from = 1, to = 128) int spawnRange();

    @NonNegative float totalMobs();

    @NonNegative float simultaneousMobs();

    @NonNegative float totalAddedMobs();

    @NonNegative float simultaneousAddedMobs();

    @NonNegative float ticksBetweenSpawn();

    @Unmodifiable List<SpawnerEntry> potentialSpawns();

    @Unmodifiable Object2IntMap<Key> potentialRewards();

    Key ominousItemsAttackChoice();

    /**
     * A mutable builder for the {@link TrialSpawnerConfigRegistryEntry} plugins may change in applicable registry events.
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder extends TrialSpawnerConfigRegistryEntry, RegistryBuilder<TrialSpawnerConfig> {

        /**
         * Sets the radius around which this trial spawner config will attempt to spawn mobs in.
         * The covered area is a square.
         *
         * @return this builder instance.
         * @see TrialSpawnerConfigRegistryEntry#spawnRange()
         * @see TrialSpawnerConfig#getSpawnRange()
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder spawnRange(@IntRange(from = 1, to = 128) int radius);

        @Contract(value = "_ -> this", mutates = "this")
        Builder totalMobs(@NonNegative float amount);

        @Contract(value = "_ -> this", mutates = "this")
        Builder simultaneousMobs(@NonNegative float amount);

        @Contract(value = "_ -> this", mutates = "this")
        Builder totalAddedMobs(@NonNegative float amount);

        @Contract(value = "_ -> this", mutates = "this")
        Builder simultaneousAddedMobs(@NonNegative float amount);

        @Contract(value = "_ -> this", mutates = "this")
        Builder ticksBetweenSpawn(@NonNegative int amount);

        // @Contract(value = "_ -> this", mutates = "this")
        // Builder potentialSpawns(List<SpawnerEntry> spawns);
        // todo implement, but no way to have an EntitySnapshot here + Lootable need to be a key, or maybe wait for RegistryRetriever

        @Contract(value = "_ -> this", mutates = "this")
        Builder potentialRewards(Map<Key, Integer> rewards);

        @Contract(value = "_ -> this", mutates = "this")
        Builder ominousItemsAttackChoice(Key lootTable);
    }
}
