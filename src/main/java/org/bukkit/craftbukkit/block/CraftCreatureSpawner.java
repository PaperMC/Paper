package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityMobSpawner;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {
    private final CraftWorld world;
    private final TileEntityMobSpawner spawner;

    public CraftCreatureSpawner(final Block block) {
        super(block);

        world = (CraftWorld) block.getWorld();
        spawner = (TileEntityMobSpawner) world.getTileEntityAt(getX(), getY(), getZ());
    }

    @Deprecated
    public CreatureType getCreatureType() {
        return CreatureType.fromName(spawner.mobName);
    }

    public EntityType getSpawnedType() {
        return EntityType.fromName(spawner.mobName);
    }

    @Deprecated
    public void setCreatureType(CreatureType creatureType) {
        spawner.mobName = creatureType.getName();
    }

    public void setSpawnedType(EntityType creatureType) {
        if (!creatureType.isAlive() || !creatureType.isSpawnable()) {
            throw new IllegalArgumentException("Can't spawn non-living entities from mob spawners!");
        }

        spawner.mobName = creatureType.getName();
    }

    public String getCreatureTypeId() {
        return spawner.mobName;
    }

    @Deprecated
    public void setCreatureTypeId(String creatureName) {
        setCreatureTypeByName(creatureName);
    }

    public String getCreatureTypeName() {
        return spawner.mobName;
    }

    public void setCreatureTypeByName(String creatureType) {
        // Verify input
        EntityType type = EntityType.fromName(creatureType);
        if (type == null) {
            return;
        }
        setSpawnedType(type);
    }

    public int getDelay() {
        return spawner.spawnDelay;
    }

    public void setDelay(int delay) {
        spawner.spawnDelay = delay;
    }

}
