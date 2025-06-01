package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerStateData;
import net.minecraft.world.level.storage.TagValueInput;
import org.bukkit.block.spawner.SpawnRule;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.entity.CraftEntitySnapshot;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.spawner.TrialSpawnerConfiguration;

public class CraftTrialSpawnerConfiguration implements TrialSpawnerConfiguration {
    private final TrialSpawnerBlockEntity snapshot;

    private int spawnRange;
    private float totalMobs;
    private float simultaneousMobs;
    private float totalMobsAddedPerPlayer;
    private float simultaneousMobsAddedPerPlayer;
    private int ticksBetweenSpawn;
    private WeightedList<SpawnData> spawnPotentialsDefinition;
    private WeightedList<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> lootTablesToEject;
    private ResourceKey<net.minecraft.world.level.storage.loot.LootTable> itemsToDropWhenOminous;

    public CraftTrialSpawnerConfiguration(TrialSpawnerConfig minecraft, TrialSpawnerBlockEntity snapshot) {
        this.snapshot = snapshot;

        this.spawnRange = minecraft.spawnRange();
        this.totalMobs = minecraft.totalMobs();
        this.simultaneousMobs = minecraft.simultaneousMobs();
        this.totalMobsAddedPerPlayer = minecraft.totalMobsAddedPerPlayer();
        this.simultaneousMobsAddedPerPlayer = minecraft.simultaneousMobsAddedPerPlayer();
        this.ticksBetweenSpawn = minecraft.ticksBetweenSpawn();
        this.spawnPotentialsDefinition = minecraft.spawnPotentialsDefinition();
        this.lootTablesToEject = minecraft.lootTablesToEject();
        this.itemsToDropWhenOminous = minecraft.itemsToDropWhenOminous();
    }

    @Override
    public EntityType getSpawnedType() {
        if (this.spawnPotentialsDefinition.isEmpty()) {
            return null;
        }

        Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.by(TagValueInput.createGlobalDiscarding(
            this.spawnPotentialsDefinition.unwrap().getFirst().value().getEntityToSpawn()
        ));
        return type.map(CraftEntityType::minecraftToBukkit).orElse(null);
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null) {
            this.getTrialData().nextSpawnData = Optional.empty();
            this.spawnPotentialsDefinition = WeightedList.of(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later
            return;
        }
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "Can't spawn EntityType %s from mob spawners!", entityType);

        SpawnData data = new SpawnData();
        data.getEntityToSpawn().putString(Entity.TAG_ID, BuiltInRegistries.ENTITY_TYPE.getKey(CraftEntityType.bukkitToMinecraft(entityType)).toString());
        this.getTrialData().nextSpawnData = Optional.of(data);
        this.spawnPotentialsDefinition = WeightedList.of(data);
    }

    @Override
    public float getBaseSpawnsBeforeCooldown() {
        return this.totalMobs;
    }

    @Override
    public void setBaseSpawnsBeforeCooldown(float amount) {
        this.totalMobs = amount;
    }

    @Override
    public float getBaseSimultaneousEntities() {
        return this.simultaneousMobs;
    }

    @Override
    public void setBaseSimultaneousEntities(float amount) {
        this.simultaneousMobs = amount;
    }

    @Override
    public float getAdditionalSpawnsBeforeCooldown() {
        return this.totalMobsAddedPerPlayer;
    }

    @Override
    public void setAdditionalSpawnsBeforeCooldown(float amount) {
        this.totalMobsAddedPerPlayer = amount;
    }

    @Override
    public float getAdditionalSimultaneousEntities() {
        return this.simultaneousMobsAddedPerPlayer;
    }

    @Override
    public void setAdditionalSimultaneousEntities(float amount) {
        this.simultaneousMobsAddedPerPlayer = amount;
    }

    @Override
    public int getDelay() {
        return this.ticksBetweenSpawn;
    }

    @Override
    public void setDelay(int delay) {
        Preconditions.checkArgument(delay >= 0, "Delay cannot be less than 0");

        this.ticksBetweenSpawn = delay;
    }

    @Override
    public int getSpawnRange() {
        return this.spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.spawnRange = spawnRange;
    }

    @Override
    public EntitySnapshot getSpawnedEntity() {
        WeightedList<SpawnData> potentials = this.spawnPotentialsDefinition;
        if (potentials.isEmpty()) {
            return null;
        }

        return CraftEntitySnapshot.create(potentials.unwrap().get(0).value().getEntityToSpawn());
    }

    @Override
    public void setSpawnedEntity(EntitySnapshot snapshot) {
        this.setSpawnedEntity(snapshot, null, null);
    }

    @Override
    public void setSpawnedEntity(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        this.setSpawnedEntity(spawnerEntry.getSnapshot(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    private void setSpawnedEntity(EntitySnapshot snapshot, SpawnRule spawnRule, SpawnerEntry.Equipment equipment) {
        if (snapshot == null) {
            this.getTrialData().nextSpawnData = Optional.empty();
            this.spawnPotentialsDefinition = WeightedList.of(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later
            return;
        }

        CompoundTag compoundTag = ((CraftEntitySnapshot) snapshot).getData();
        SpawnData data = new SpawnData(compoundTag, Optional.ofNullable(CraftCreatureSpawner.toMinecraftRule(spawnRule)), CraftCreatureSpawner.getEquipment(equipment));

        this.getTrialData().nextSpawnData = Optional.of(data);
        this.spawnPotentialsDefinition = WeightedList.of(data);
    }

    @Override
    public void addPotentialSpawn(EntitySnapshot snapshot, int weight, SpawnRule spawnRule) {
        this.addPotentialSpawn(snapshot, weight, spawnRule, null);
    }

    private void addPotentialSpawn(EntitySnapshot snapshot, int weight, SpawnRule spawnRule, SpawnerEntry.Equipment equipment) {
        Preconditions.checkArgument(snapshot != null, "Snapshot cannot be null");

        CompoundTag compoundTag = ((CraftEntitySnapshot) snapshot).getData();

        WeightedList.Builder<SpawnData> builder = WeightedList.builder();
        this.spawnPotentialsDefinition.unwrap().forEach(entry -> builder.add(entry.value(), entry.weight()));
        builder.add(new SpawnData(compoundTag, Optional.ofNullable(CraftCreatureSpawner.toMinecraftRule(spawnRule)), CraftCreatureSpawner.getEquipment(equipment)), weight);
        this.spawnPotentialsDefinition = builder.build();
    }

    @Override
    public void addPotentialSpawn(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        this.addPotentialSpawn(spawnerEntry.getSnapshot(), spawnerEntry.getSpawnWeight(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    @Override
    public void setPotentialSpawns(Collection<SpawnerEntry> entries) {
        Preconditions.checkArgument(entries != null, "Entries cannot be null");

        WeightedList.Builder<SpawnData> builder = WeightedList.builder();
        for (SpawnerEntry spawnerEntry : entries) {
            CompoundTag compoundTag = ((CraftEntitySnapshot) spawnerEntry.getSnapshot()).getData();
            builder.add(new SpawnData(compoundTag, Optional.ofNullable(CraftCreatureSpawner.toMinecraftRule(spawnerEntry.getSpawnRule())), CraftCreatureSpawner.getEquipment(spawnerEntry.getEquipment())), spawnerEntry.getSpawnWeight());
        }
        this.spawnPotentialsDefinition = builder.build();
    }

    @Override
    public List<SpawnerEntry> getPotentialSpawns() {
        List<SpawnerEntry> entries = new ArrayList<>();

        for (Weighted<SpawnData> entry : this.spawnPotentialsDefinition.unwrap()) {
            CraftEntitySnapshot snapshot = CraftEntitySnapshot.create(entry.value().getEntityToSpawn());

            if (snapshot != null) {
                SpawnRule rule = entry.value().customSpawnRules().map(CraftCreatureSpawner::fromMinecraftRule).orElse(null);
                entries.add(new SpawnerEntry(snapshot, entry.weight(), rule));
            }
        }
        return entries;
    }

    @Override
    public Map<LootTable, Integer> getPossibleRewards() {
        Map<LootTable, Integer> tables = new HashMap<>();

        for (Weighted<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> entry : this.lootTablesToEject.unwrap()) {
            LootTable table = CraftLootTable.minecraftToBukkit(entry.value());
            if (table != null) {
                tables.put(table, entry.weight());
            }
        }

        return tables;
    }

    @Override
    public void addPossibleReward(LootTable table, int weight) {
        Preconditions.checkArgument(table != null, "Table cannot be null");
        Preconditions.checkArgument(weight >= 1, "Weight must be at least 1");

        WeightedList.Builder<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> builder = WeightedList.builder();
        this.lootTablesToEject.unwrap().forEach(entry -> builder.add(entry.value(), entry.weight()));
        builder.add(CraftLootTable.bukkitToMinecraft(table), weight);
        this.lootTablesToEject = builder.build();
    }

    @Override
    public void removePossibleReward(LootTable table) {
        Preconditions.checkArgument(table != null, "Key cannot be null");

        ResourceKey<net.minecraft.world.level.storage.loot.LootTable> minecraftKey = CraftLootTable.bukkitToMinecraft(table);
        WeightedList.Builder<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> builder = WeightedList.builder();

        for (Weighted<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> entry : this.lootTablesToEject.unwrap()) {
            if (!entry.value().equals(minecraftKey)) {
                builder.add(entry.value(), entry.weight());
            }
        }
        this.lootTablesToEject = builder.build();
    }

    @Override
    public void setPossibleRewards(Map<LootTable, Integer> rewards) {
        if (rewards == null || rewards.isEmpty()) {
            this.lootTablesToEject = WeightedList.of();
            return;
        }

        WeightedList.Builder<ResourceKey<net.minecraft.world.level.storage.loot.LootTable>> builder = WeightedList.builder();
        rewards.forEach((table, weight) -> {
            Preconditions.checkArgument(table != null, "Table cannot be null");
            Preconditions.checkArgument(weight >= 1, "Weight must be at least 1");

            builder.add(CraftLootTable.bukkitToMinecraft(table), weight);
        });

        this.lootTablesToEject = builder.build();
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.snapshot.trialSpawner.getRequiredPlayerRange();
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.snapshot.trialSpawner.config = this.snapshot.trialSpawner.config.overrideRequiredPlayerRange(requiredPlayerRange);
    }

    private TrialSpawnerStateData getTrialData() {
        return this.snapshot.getTrialSpawner().getStateData();
    }

    protected TrialSpawnerConfig toMinecraft() {
        return new TrialSpawnerConfig(this.spawnRange, this.totalMobs, this.simultaneousMobs, this.totalMobsAddedPerPlayer, this.simultaneousMobsAddedPerPlayer, this.ticksBetweenSpawn, this.spawnPotentialsDefinition, this.lootTablesToEject, this.itemsToDropWhenOminous);
    }
}
