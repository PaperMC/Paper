package io.papermc.paper.block;

import io.papermc.paper.registry.HolderableBase;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperTrialSpawnerConfig extends HolderableBase<TrialSpawnerConfig> implements io.papermc.paper.block.TrialSpawnerConfig {

    public static Holder<TrialSpawnerConfig> bukkitToMinecraftHolder(final io.papermc.paper.block.TrialSpawnerConfig config) {
        return CraftRegistry.bukkitToMinecraftHolder(config);
    }

    public static io.papermc.paper.block.TrialSpawnerConfig minecraftHolderToBukkit(final Holder<TrialSpawnerConfig> config) {
        return CraftRegistry.minecraftHolderToBukkit(config, Registries.TRIAL_SPAWNER_CONFIG);
    }

    public PaperTrialSpawnerConfig(final Holder<TrialSpawnerConfig> holder) {
        super(holder);
    }

    @Override
    public int getSpawnRange() {
        return this.getHandle().spawnRange();
    }

    @Override
    public float getTotalMobs() {
        return this.getHandle().totalMobs();
    }

    @Override
    public float getSimultaneousMobs() {
        return this.getHandle().simultaneousMobs();
    }

    @Override
    public float getTotalAddedMobs() {
        return this.getHandle().totalMobsAddedPerPlayer();
    }

    @Override
    public float getSimultaneousAddedMobs() {
        return this.getHandle().simultaneousMobsAddedPerPlayer();
    }

    @Override
    public int getTicksBetweenSpawn() {
        return this.getHandle().ticksBetweenSpawn();
    }

    @Override
    public List<SpawnerEntry> potentialSpawns() {
        List<SpawnerEntry> spawns = new ObjectArrayList<>();
        for (Weighted<SpawnData> data : this.getHandle().spawnPotentialsDefinition().unwrap()) {
            SpawnerEntry entry = CraftCreatureSpawner.makeSpawnEntry(data);
            if (entry != null) {
                spawns.add(entry);
            }
        }
        return Collections.unmodifiableList(spawns);
    }

    @Override
    public Object2IntMap<LootTable> potentialRewards() {
        List<Weighted<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>>> looTables = this.getHandle().lootTablesToEject().unwrap();
        Object2IntMap<LootTable> rewards = new Object2IntOpenHashMap<>(looTables.size());
        for (Weighted<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTable : looTables) {
            rewards.put(CraftLootTable.minecraftToBukkit(lootTable.value()), lootTable.weight());
        }
        return Object2IntMaps.unmodifiable(rewards);
    }

    @Override
    public LootTable ominousItemsAttackChoice() {
        return CraftLootTable.minecraftToBukkit(this.getHandle().itemsToDropWhenOminous());
    }
}
