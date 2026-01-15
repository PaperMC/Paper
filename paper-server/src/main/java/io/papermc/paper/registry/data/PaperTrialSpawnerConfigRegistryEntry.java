package io.papermc.paper.registry.data;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.util.Conversions;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.kyori.adventure.key.Key;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgumentMin;
import static io.papermc.paper.registry.data.util.Checks.asArgumentRange;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperTrialSpawnerConfigRegistryEntry implements TrialSpawnerConfigRegistryEntry {

    protected final Conversions conversions;
    protected int spawnRange = TrialSpawnerConfig.DEFAULT.spawnRange();
    protected float totalMobs = TrialSpawnerConfig.DEFAULT.totalMobs();
    protected float simultaneousMobs = TrialSpawnerConfig.DEFAULT.simultaneousMobs();
    protected float totalMobsAddedPerPlayer = TrialSpawnerConfig.DEFAULT.totalMobsAddedPerPlayer();
    protected float simultaneousMobsAddedPerPlayer = TrialSpawnerConfig.DEFAULT.simultaneousMobsAddedPerPlayer();
    protected int ticksBetweenSpawn = TrialSpawnerConfig.DEFAULT.ticksBetweenSpawn();
    protected WeightedList<SpawnData> spawnPotentialsDefinition = TrialSpawnerConfig.DEFAULT.spawnPotentialsDefinition();
    protected WeightedList<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTablesToEject = TrialSpawnerConfig.DEFAULT.lootTablesToEject();
    protected ResourceKey<net.minecraft.world.level.storage.loot.LootTable> itemsToDropWhenOminous = TrialSpawnerConfig.DEFAULT.itemsToDropWhenOminous();

    public PaperTrialSpawnerConfigRegistryEntry(final Conversions conversions, final @Nullable TrialSpawnerConfig internal) {
        this.conversions = conversions;
        if (internal == null) {
            return;
        }

        this.spawnRange = internal.spawnRange();
        this.totalMobs = internal.totalMobs();
        this.simultaneousMobs = internal.simultaneousMobs();
        this.totalMobsAddedPerPlayer = internal.totalMobsAddedPerPlayer();
        this.simultaneousMobsAddedPerPlayer = internal.simultaneousMobsAddedPerPlayer();
        this.ticksBetweenSpawn = internal.ticksBetweenSpawn();
        this.spawnPotentialsDefinition = internal.spawnPotentialsDefinition();
        this.lootTablesToEject = internal.lootTablesToEject();
        this.itemsToDropWhenOminous = internal.itemsToDropWhenOminous();
    }

    @Override
    public @IntRange(from = 1, to = 128) int spawnRange() {
        return this.spawnRange;
    }

    @Override
    public @NonNegative float totalMobs() {
        return this.totalMobs;
    }

    @Override
    public @NonNegative float simultaneousMobs() {
        return this.simultaneousMobs;
    }

    @Override
    public @NonNegative float totalAddedMobs() {
        return this.totalMobsAddedPerPlayer;
    }

    @Override
    public @NonNegative float simultaneousAddedMobs() {
        return this.simultaneousMobsAddedPerPlayer;
    }

    @Override
    public @NonNegative float ticksBetweenSpawn() {
        return this.ticksBetweenSpawn;
    }

    @Override
    public @Unmodifiable List<SpawnerEntry> potentialSpawns() {
        List<SpawnerEntry> spawns = new ObjectArrayList<>();
        for (Weighted<SpawnData> data : asConfigured(this.spawnPotentialsDefinition, "spawnPotentialsDefinition").unwrap()) {
            SpawnerEntry entry = CraftCreatureSpawner.makeSpawnEntry(data);
            if (entry != null) {
                spawns.add(entry);
            }
        }
        return Collections.unmodifiableList(spawns);
    }

    @Override
    public @Unmodifiable Object2IntMap<Key> potentialRewards() {
        List<Weighted<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>>> looTables = asConfigured(this.lootTablesToEject, "lootTablesToEject").unwrap();
        Object2IntMap<Key> rewards = new Object2IntOpenHashMap<>(looTables.size());
        for (Weighted<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTable : looTables) {
            rewards.put(PaperAdventure.asAdventure(lootTable.value().identifier()), lootTable.weight());
        }
        return Object2IntMaps.unmodifiable(rewards);
    }

    @Override
    public Key ominousItemsAttackChoice() {
        return PaperAdventure.asAdventure(asConfigured(this.itemsToDropWhenOminous, "this.itemsToDropWhenOminous").identifier());
    }

    public static final class PaperBuilder extends PaperTrialSpawnerConfigRegistryEntry implements Builder, PaperRegistryBuilder<TrialSpawnerConfig, io.papermc.paper.block.TrialSpawnerConfig> {

        public PaperBuilder(final Conversions conversions, final @Nullable TrialSpawnerConfig internal) {
            super(conversions, internal);
        }

        @Override
        public Builder spawnRange(final @IntRange(from = 1, to = 128) int radius) {
            this.spawnRange = asArgumentRange(radius, "spawnRange", 1, 128);
            return this;
        }

        @Override
        public Builder totalMobs(final @NonNegative float amount) {
            this.totalMobs = asArgumentMin(amount, "totalMobs", 0.0F);
            return this;
        }

        @Override
        public Builder simultaneousMobs(final @NonNegative float amount) {
            this.simultaneousMobs = asArgumentMin(amount, "simultaneousMobs", 0.0F);
            return this;
        }

        @Override
        public Builder totalAddedMobs(final @NonNegative float amount) {
            this.totalMobsAddedPerPlayer = asArgumentMin(amount, "totalMobsAddedPerPlayer", 0.0F);
            return this;
        }

        @Override
        public Builder simultaneousAddedMobs(final @NonNegative float amount) {
            this.simultaneousMobsAddedPerPlayer = asArgumentMin(amount, "simultaneousMobsAddedPerPlayer", 0.0F);
            return this;
        }

        @Override
        public Builder ticksBetweenSpawn(final @NonNegative int amount) {
            this.ticksBetweenSpawn = asArgumentMin(amount, "ticksBetweenSpawn", 0);
            return this;
        }

        /*
        @Override
        public Builder potentialSpawns(final List<SpawnerEntry> spawns) {
            WeightedList.Builder<SpawnData> spawnPotentialsDefinition = WeightedList.builder();
            for (SpawnerEntry entry : asConfigured(spawns, "spawns")) {
                spawnPotentialsDefinition.add(CraftCreatureSpawner.extractSpawnData(entry), entry.getSpawnWeight());
            }
            this.spawnPotentialsDefinition = spawnPotentialsDefinition.build();
            return this;
        }*/

        @Override
        public Builder potentialRewards(final Map<Key, Integer> rewards) {
            WeightedList.Builder<ResourceKey<LootTable>> lootTablesToEject = WeightedList.builder();
            for (Map.Entry<Key, Integer> entry : asConfigured(rewards, "rewards").entrySet()) {
                lootTablesToEject.add(PaperAdventure.asVanilla(Registries.LOOT_TABLE, entry.getKey()), entry.getValue());
            }
            this.lootTablesToEject = lootTablesToEject.build();
            return this;
        }

        @Override
        public Builder ominousItemsAttackChoice(final Key lootTable) {
            this.itemsToDropWhenOminous = PaperAdventure.asVanilla(Registries.LOOT_TABLE, asConfigured(lootTable, "lootTable"));
            return this;
        }

        @Override
        public TrialSpawnerConfig build() {
            return new TrialSpawnerConfig(
                this.spawnRange,
                this.totalMobs,
                this.simultaneousMobs,
                this.totalMobsAddedPerPlayer,
                this.simultaneousMobsAddedPerPlayer,
                this.ticksBetweenSpawn,
                this.spawnPotentialsDefinition,
                this.lootTablesToEject,
                this.itemsToDropWhenOminous
            );
        }
    }
}
