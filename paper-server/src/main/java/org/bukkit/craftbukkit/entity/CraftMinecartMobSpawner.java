package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.vehicle.EntityMinecartMobSpawner;
import net.minecraft.world.level.MobSpawnerData;
import org.bukkit.block.spawner.SpawnRule;
import org.bukkit.block.spawner.SpawnerEntry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftCreatureSpawner;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.minecart.SpawnerMinecart;

final class CraftMinecartMobSpawner extends CraftMinecart implements SpawnerMinecart {
    CraftMinecartMobSpawner(CraftServer server, EntityMinecartMobSpawner entity) {
        super(server, entity);
    }

    @Override
    public EntityType getSpawnedType() {
        MobSpawnerData spawnData = getHandle().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        Optional<EntityTypes<?>> type = EntityTypes.by(spawnData.getEntityToSpawn());
        return type.map(CraftEntityType::minecraftToBukkit).orElse(null);
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null) {
            getHandle().getSpawner().spawnPotentials = SimpleWeightedRandomList.empty(); // need clear the spawnPotentials to avoid nextSpawnData being replaced later
            getHandle().getSpawner().nextSpawnData = new MobSpawnerData();
            return;
        }
        Preconditions.checkArgument(entityType != EntityType.UNKNOWN, "Can't spawn EntityType %s from mob spawners!", entityType);

        RandomSource rand = getHandle().level().getRandom();
        getHandle().getSpawner().setEntityId(CraftEntityType.bukkitToMinecraft(entityType), getHandle().level(), rand, getHandle().blockPosition());
    }

    @Override
    public EntitySnapshot getSpawnedEntity() {
        MobSpawnerData spawnData = getHandle().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return null;
        }

        return CraftEntitySnapshot.create(spawnData.getEntityToSpawn());
    }

    @Override
    public void setSpawnedEntity(EntitySnapshot snapshot) {
        CraftCreatureSpawner.setSpawnedEntity(getHandle().getSpawner(), snapshot, null, null);
    }

    @Override
    public void setSpawnedEntity(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        CraftCreatureSpawner.setSpawnedEntity(getHandle().getSpawner(), spawnerEntry.getSnapshot(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    @Override
    public void addPotentialSpawn(EntitySnapshot snapshot, int weight, SpawnRule spawnRule) {
        CraftCreatureSpawner.addPotentialSpawn(getHandle().getSpawner(), snapshot, weight, spawnRule, null);
    }

    @Override
    public void addPotentialSpawn(SpawnerEntry spawnerEntry) {
        Preconditions.checkArgument(spawnerEntry != null, "Entry cannot be null");

        CraftCreatureSpawner.addPotentialSpawn(getHandle().getSpawner(), spawnerEntry.getSnapshot(), spawnerEntry.getSpawnWeight(), spawnerEntry.getSpawnRule(), spawnerEntry.getEquipment());
    }

    @Override
    public void setPotentialSpawns(Collection<SpawnerEntry> entries) {
        CraftCreatureSpawner.setPotentialSpawns(getHandle().getSpawner(), entries);
    }

    @Override
    public List<SpawnerEntry> getPotentialSpawns() {
        return CraftCreatureSpawner.getPotentialSpawns(getHandle().getSpawner());
    }

    @Override
    public int getDelay() {
        return getHandle().getSpawner().spawnDelay;
    }

    @Override
    public void setDelay(int delay) {
        getHandle().getSpawner().spawnDelay = delay;
    }

    @Override
    public int getMinSpawnDelay() {
        return getHandle().getSpawner().minSpawnDelay;
    }

    @Override
    public void setMinSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay <= getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        getHandle().getSpawner().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return getHandle().getSpawner().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
        getHandle().getSpawner().maxSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxNearbyEntities() {
        return getHandle().getSpawner().maxNearbyEntities;
    }

    @Override
    public void setMaxNearbyEntities(int maxNearbyEntities) {
        getHandle().getSpawner().maxNearbyEntities = maxNearbyEntities;
    }

    @Override
    public int getSpawnCount() {
        return getHandle().getSpawner().spawnCount;
    }

    @Override
    public void setSpawnCount(int count) {
        getHandle().getSpawner().spawnCount = count;
    }

    @Override
    public int getRequiredPlayerRange() {
        return getHandle().getSpawner().requiredPlayerRange;
    }

    @Override
    public void setRequiredPlayerRange(int requiredPlayerRange) {
        getHandle().getSpawner().requiredPlayerRange = requiredPlayerRange;
    }

    @Override
    public int getSpawnRange() {
        return getHandle().getSpawner().spawnRange;
    }

    @Override
    public void setSpawnRange(int spawnRange) {
        getHandle().getSpawner().spawnRange = spawnRange;
    }

    @Override
    public EntityMinecartMobSpawner getHandle() {
        return (EntityMinecartMobSpawner) entity;
    }

    @Override
    public String toString() {
        return "CraftMinecartMobSpawner";
    }
}
