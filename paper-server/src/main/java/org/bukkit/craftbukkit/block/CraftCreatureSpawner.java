package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EquipmentTable;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.spawner.SpawnRule;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.entity.CraftEntitySnapshot;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<SpawnerBlockEntity> implements CreatureSpawner, org.bukkit.craftbukkit.spawner.PaperSharedSpawnerLogic { // Paper - more spawner API

    public CraftCreatureSpawner(World world, SpawnerBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftCreatureSpawner(CraftCreatureSpawner state, Location location) {
        super(state, location);
    }

    @Override
    public EntityType getSpawnedType() {
        SpawnData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(() -> "spawner@" + getLocation(), LOGGER)) {
            ValueInput valueInput = TagValueInput.create(scopedCollector, this.getInternalWorld().registryAccess(), spawnData.entityToSpawn());
            Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.by(valueInput);
            return type.map(CraftEntityType::minecraftToBukkit).orElse(null);
        }
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null) {
            this.getSnapshot().getSpawner().spawnPotentials = WeightedList.of(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later
            this.getSnapshot().getSpawner().nextSpawnData = new SpawnData();
            return;
        }
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "Can't spawn EntityType %s from mob spawners!", entityType);

        RandomSource rand = (this.isPlaced()) ? this.getWorldHandle().getRandom() : RandomSource.create();
        this.getSnapshot().setEntityId(CraftEntityType.bukkitToMinecraft(entityType), rand);
    }

    @Override
    public EntitySnapshot getSpawnedEntity() {
        SpawnData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        return CraftEntitySnapshot.create(spawnData.getEntityToSpawn());
    }

    @Override
    public void setSpawnedEntity(EntitySnapshot snapshot) {
        CraftCreatureSpawner.setSpawnedEntity(this.getSnapshot().getSpawner(), snapshot, null, null);
    }

    @Override
    public void setSpawnedEntity(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        CraftCreatureSpawner.setSpawnedEntity(this.getSnapshot().getSpawner(), spawnerEntry.getSnapshot(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    public static void setSpawnedEntity(BaseSpawner spawner, EntitySnapshot snapshot, SpawnRule spawnRule, SpawnerEntry.Equipment equipment) {
        spawner.spawnPotentials = WeightedList.of(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later

        if (snapshot == null) {
            spawner.nextSpawnData = new SpawnData();
            return;
        }
        CompoundTag compoundTag = ((CraftEntitySnapshot) snapshot).getData();

        spawner.nextSpawnData = new SpawnData(compoundTag, Optional.ofNullable(CraftCreatureSpawner.toMinecraftRule(spawnRule)), CraftCreatureSpawner.getEquipment(equipment));
    }

    @Override
    public void addPotentialSpawn(EntitySnapshot snapshot, int weight, SpawnRule spawnRule) {
        CraftCreatureSpawner.addPotentialSpawn(this.getSnapshot().getSpawner(), snapshot, weight, spawnRule, null);
    }

    public static void addPotentialSpawn(BaseSpawner spawner, EntitySnapshot snapshot, int weight, SpawnRule spawnRule, SpawnerEntry.Equipment equipment) {
        Preconditions.checkArgument(snapshot != null, "Snapshot cannot be null");

        CompoundTag compoundTag = ((CraftEntitySnapshot) snapshot).getData();

        WeightedList.Builder<SpawnData> builder = WeightedList.builder();
        spawner.spawnPotentials.unwrap().forEach(entry -> builder.add(entry.value(), entry.weight()));
        builder.add(new SpawnData(compoundTag, Optional.ofNullable(CraftCreatureSpawner.toMinecraftRule(spawnRule)), CraftCreatureSpawner.getEquipment(equipment)), weight);
        spawner.spawnPotentials = builder.build();
    }

    @Override
    public void addPotentialSpawn(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        this.addPotentialSpawn(spawnerEntry.getSnapshot(), spawnerEntry.getSpawnWeight(), spawnerEntry.getSpawnRule());
    }

    @Override
    public void setPotentialSpawns(Collection<SpawnerEntry> entries) {
        CraftCreatureSpawner.setPotentialSpawns(this.getSnapshot().getSpawner(), entries);
    }

    public static void setPotentialSpawns(BaseSpawner spawner, Collection<SpawnerEntry> entries) {
        Preconditions.checkArgument(entries != null, "Entries cannot be null");

        WeightedList.Builder<SpawnData> builder = WeightedList.builder();
        for (SpawnerEntry spawnerEntry : entries) {
            CompoundTag compoundTag = ((CraftEntitySnapshot) spawnerEntry.getSnapshot()).getData();
            builder.add(new SpawnData(compoundTag, Optional.ofNullable(CraftCreatureSpawner.toMinecraftRule(spawnerEntry.getSpawnRule())), CraftCreatureSpawner.getEquipment(spawnerEntry.getEquipment())), spawnerEntry.getSpawnWeight());
        }
        spawner.spawnPotentials = builder.build();
    }

    @Override
    public List<SpawnerEntry> getPotentialSpawns() {
        return CraftCreatureSpawner.getPotentialSpawns(this.getSnapshot().getSpawner());
    }

    public static List<SpawnerEntry> getPotentialSpawns(BaseSpawner spawner) {
        List<SpawnerEntry> entries = new ArrayList<>();

        for (Weighted<SpawnData> entry : spawner.spawnPotentials.unwrap()) {
            CraftEntitySnapshot snapshot = CraftEntitySnapshot.create(entry.value().getEntityToSpawn());

            if (snapshot != null) {
                SpawnRule rule = entry.value().customSpawnRules().map(CraftCreatureSpawner::fromMinecraftRule).orElse(null);
                entries.add(new SpawnerEntry(snapshot, entry.weight(), rule, CraftCreatureSpawner.getEquipment(entry.value().equipment())));
            }
        }
        return entries;
    }

    public static SpawnData.CustomSpawnRules toMinecraftRule(SpawnRule rule) {
        if (rule == null) {
            return null;
        }
        return new SpawnData.CustomSpawnRules(new InclusiveRange<>(rule.getMinBlockLight(), rule.getMaxBlockLight()), new InclusiveRange<>(rule.getMinSkyLight(), rule.getMaxSkyLight()));
    }

    public static SpawnRule fromMinecraftRule(SpawnData.CustomSpawnRules rule) {
        InclusiveRange<Integer> blockLight = rule.blockLightLimit();
        InclusiveRange<Integer> skyLight = rule.skyLightLimit();

        return new SpawnRule(blockLight.maxInclusive(), blockLight.maxInclusive(), skyLight.minInclusive(), skyLight.maxInclusive());
    }

    @Override
    public String getCreatureTypeName() {
        SpawnData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(() -> "spawner@" + getLocation(), LOGGER)) {
            ValueInput valueInput = TagValueInput.create(scopedCollector, this.getInternalWorld().registryAccess(), spawnData.getEntityToSpawn());
            Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.by(valueInput);

            return type.map(CraftEntityType::minecraftToBukkit).map(CraftEntityType::bukkitToString).orElse(null);
        }
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = CraftEntityType.stringToBukkit(creatureType);
        if (type == null) {
            this.setSpawnedType(null);
            return;
        }
        this.setSpawnedType(type);
    }

    @Override
    public int getDelay() {
        return this.getSnapshot().getSpawner().spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        this.getSnapshot().getSpawner().spawnDelay = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return this.getSnapshot().getSpawner().minSpawnDelay;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= this.getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getSnapshot().getSpawner().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getSnapshot().getSpawner().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= this.getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        this.getSnapshot().getSpawner().maxSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.getSnapshot().getSpawner().maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        this.getSnapshot().getSpawner().maxNearbyEntities = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return this.getSnapshot().getSpawner().spawnCount;
    }

    @Override
    public void setSpawnCount(int count) {
        this.getSnapshot().getSpawner().spawnCount = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getSnapshot().getSpawner().requiredPlayerRange;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getSnapshot().getSpawner().requiredPlayerRange = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return this.getSnapshot().getSpawner().spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.getSnapshot().getSpawner().spawnRange = spawnRange;
    }

    @Override
    public CraftCreatureSpawner copy() {
        return new CraftCreatureSpawner(this, null);
    }

    @Override
    public CraftCreatureSpawner copy(Location location) {
        return new CraftCreatureSpawner(this, location);
    }

    public static Optional<EquipmentTable> getEquipment(SpawnerEntry.Equipment bukkit) {
        if (bukkit == null) {
            return Optional.empty();
        }

        return Optional.of(new EquipmentTable(
                CraftLootTable.bukkitToMinecraft(bukkit.getEquipmentLootTable()),
                bukkit.getDropChances().entrySet().stream().collect(Collectors.toMap((entry) -> CraftEquipmentSlot.getNMS(entry.getKey()), Map.Entry::getValue)))
        );
    }

    public static SpawnerEntry.Equipment getEquipment(Optional<EquipmentTable> optional) {
        return optional.map((nms) -> new SpawnerEntry.Equipment(
                CraftLootTable.minecraftToBukkit(nms.lootTable()),
                new HashMap<>(nms.slotDropChances().entrySet().stream().collect(Collectors.toMap((entry) -> CraftEquipmentSlot.getSlot(entry.getKey()), Map.Entry::getValue)))
        )).orElse(null);
    }

    @Override
    public boolean isActivated() {
        requirePlaced();
        return org.bukkit.craftbukkit.spawner.PaperSharedSpawnerLogic.super.isActivated();
    }

    @Override
    public void resetTimer() {
        requirePlaced();
        org.bukkit.craftbukkit.spawner.PaperSharedSpawnerLogic.super.resetTimer();
    }

    @Override
    public void setNextSpawnData(final SpawnData spawnData) {
        this.getSpawner().setNextSpawnData(this.isPlaced() ? this.getInternalWorld() : null, this.getInternalPosition(), spawnData);
    }

    @Override
    public BaseSpawner getSpawner() {
        return this.getSnapshot().getSpawner();
    }

    @Override
    public net.minecraft.core.BlockPos getInternalPosition() {
        return this.getPosition();
    }

    @Override
    public net.minecraft.world.level.Level getInternalWorld() {
        return this.world.getHandle();
    }
}
