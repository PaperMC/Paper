package io.papermc.paper.block;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.InlinedRegistryBuilderProvider;
import io.papermc.paper.registry.data.TrialSpawnerConfigRegistryEntry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.function.Consumer;
import net.kyori.adventure.key.Key;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.loot.LootTable;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TrialSpawnerConfig extends Keyed {

    /**
     * Creates an inlined trial spawner config.
     *
     * @param value a consumer for the builder factory
     * @return the created trial spawner config
     */
    @ApiStatus.Experimental
    static TrialSpawnerConfig create(final Consumer<RegistryBuilderFactory<TrialSpawnerConfig, ? extends TrialSpawnerConfigRegistryEntry.Builder>> value) {
        return InlinedRegistryBuilderProvider.instance().createTrialSpawnerConfig(value);
    }

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link RegistryAccess#getRegistry(RegistryKey)},
     * and {@link RegistryKey#TRIAL_SPAWNER_CONFIG}. Trial spawner configs can exist without a key.
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    @Override
    NamespacedKey getKey();

    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link RegistryAccess#getRegistry(RegistryKey)},
     * and {@link RegistryKey#TRIAL_SPAWNER_CONFIG}. Trial spawner configs can exist without a key.
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    @Override
    default Key key() {
        return Keyed.super.key();
    }

    @IntRange(from = 1, to = 128) int getSpawnRange();

    @NonNegative float getTotalMobs();

    @NonNegative float getSimultaneousMobs();

    @NonNegative float getTotalAddedMobs();

    @NonNegative float getSimultaneousAddedMobs();

    @NonNegative int getTicksBetweenSpawn();

    @Unmodifiable List<SpawnerEntry> potentialSpawns();

    @Unmodifiable Object2IntMap<LootTable> potentialRewards();

    LootTable ominousItemsAttackChoice();
}
