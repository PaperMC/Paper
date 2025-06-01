package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.storage.TagValueInput;
import org.bukkit.block.spawner.SpawnRule;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;

public class CraftMinecartMobSpawner extends CraftMinecart implements SpawnerMinecart, org.bukkit.craftbukkit.spawner.PaperSharedSpawnerLogic { // Paper - more spawner API

    public CraftMinecartMobSpawner(CraftServer server, MinecartSpawner entity) {
        super(server, entity);
    }

    @Override
    public MinecartSpawner getHandle() {
        return (MinecartSpawner) this.entity;
    }

    @Override
    public EntityType getSpawnedType() {
        SpawnData spawnData = this.getHandle().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        Optional<net.minecraft.world.entity.EntityType<?>> type = net.minecraft.world.entity.EntityType.by(
            TagValueInput.createGlobalDiscarding(spawnData.getEntityToSpawn())
        );
        return type.map(CraftEntityType::minecraftToBukkit).orElse(null);
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null) {
            this.getHandle().getSpawner().spawnPotentials = WeightedList.of(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later
            this.getHandle().getSpawner().nextSpawnData = new SpawnData();
            return;
        }
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "Can't spawn EntityType %s from mob spawners!", entityType);

        RandomSource rand = this.getHandle().level().getRandom();
        this.getHandle().getSpawner().setEntityId(CraftEntityType.bukkitToMinecraft(entityType), this.getHandle().level(), rand, this.getHandle().blockPosition());
    }

    @Override
    public EntitySnapshot getSpawnedEntity() {
        SpawnData spawnData = this.getHandle().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        return CraftEntitySnapshot.create(spawnData.getEntityToSpawn());
    }

    @Override
    public void setSpawnedEntity(EntitySnapshot snapshot) {
        CraftCreatureSpawner.setSpawnedEntity(this.getHandle().getSpawner(), snapshot, null, null);
    }

    @Override
    public void setSpawnedEntity(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        CraftCreatureSpawner.setSpawnedEntity(this.getHandle().getSpawner(), spawnerEntry.getSnapshot(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    @Override
    public void addPotentialSpawn(EntitySnapshot snapshot, int weight, SpawnRule spawnRule) {
        CraftCreatureSpawner.addPotentialSpawn(this.getHandle().getSpawner(), snapshot, weight, spawnRule, null);
    }

    @Override
    public void addPotentialSpawn(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        CraftCreatureSpawner.addPotentialSpawn(this.getHandle().getSpawner(), spawnerEntry.getSnapshot(), spawnerEntry.getSpawnWeight(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    @Override
    public void setPotentialSpawns(Collection<SpawnerEntry> entries) {
        CraftCreatureSpawner.setPotentialSpawns(this.getHandle().getSpawner(), entries);
    }

    @Override
    public List<SpawnerEntry> getPotentialSpawns() {
        return CraftCreatureSpawner.getPotentialSpawns(this.getHandle().getSpawner());
    }

    @Override
    public int getDelay() {
        return this.getHandle().getSpawner().spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        this.getHandle().getSpawner().spawnDelay = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return this.getHandle().getSpawner().minSpawnDelay;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= this.getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getHandle().getSpawner().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getHandle().getSpawner().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= this.getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        this.getHandle().getSpawner().maxSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return this.getHandle().getSpawner().maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        this.getHandle().getSpawner().maxNearbyEntities = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return this.getHandle().getSpawner().spawnCount;
    }

    @Override
    public void setSpawnCount(int count) {
        this.getHandle().getSpawner().spawnCount = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return this.getHandle().getSpawner().requiredPlayerRange;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        this.getHandle().getSpawner().requiredPlayerRange = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return this.getHandle().getSpawner().spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        this.getHandle().getSpawner().spawnRange = spawnRange;
    }

    @Override
    public net.minecraft.world.level.BaseSpawner getSpawner() {
        return this.getHandle().getSpawner();
    }

    @Override
    public net.minecraft.world.level.Level getInternalWorld() {
        return this.getHandle().level();
    }

    @Override
    public net.minecraft.core.BlockPos getInternalPosition() {
        return this.getHandle().blockPosition();
    }
}
