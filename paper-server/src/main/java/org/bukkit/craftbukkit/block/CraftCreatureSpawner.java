package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.Optional;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.MobSpawnerData;
import net.minecraft.world.level.block.entity.TileEntityMobSpawner;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<TileEntityMobSpawner> implements CreatureSpawner {

    public CraftCreatureSpawner(World world, TileEntityMobSpawner tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public EntityType getSpawnedType() {
        MobSpawnerData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return EntityType.PIG; // TODO: Change API contract to nullable?
        }

        Optional<EntityTypes<?>> type = EntityTypes.by(spawnData.getEntityToSpawn());
        return (type.isEmpty()) ? EntityType.PIG : EntityType.fromName(EntityTypes.getKey(type.get()).getPath());
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        RandomSource rand = (this.isPlaced()) ? this.getWorldHandle().getRandom() : RandomSource.create();
        this.getSnapshot().setEntityId(EntityTypes.byString(entityType.getName()).get(), rand);
    }

    @Override
    public String getCreatureTypeName() {
        MobSpawnerData spawnData = this.getSnapshot().getSpawner().nextSpawnData;
        if (spawnData == null) {
            return ""; // TODO: Change API contract to nullable?
        }

        Optional<EntityTypes<?>> type = EntityTypes.by(spawnData.getEntityToSpawn());
        return (type.isEmpty()) ? "" : EntityTypes.getKey(type.get()).getPath();
    }

    @Override
    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
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
        Preconditions.checkArgument(spawnDelay <= getMaxSpawnDelay(), "Minimum Spawn Delay must be less than or equal to Maximum Spawn Delay");
        this.getSnapshot().getSpawner().minSpawnDelay = spawnDelay;
    }

    @Override
    public int getMaxSpawnDelay() {
        return this.getSnapshot().getSpawner().maxSpawnDelay;
    }

    @Override
    public void setMaxSpawnDelay(int spawnDelay) {
        Preconditions.checkArgument(spawnDelay > 0, "Maximum Spawn Delay must be greater than 0.");
        Preconditions.checkArgument(spawnDelay >= getMinSpawnDelay(), "Maximum Spawn Delay must be greater than or equal to Minimum Spawn Delay");
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
}
