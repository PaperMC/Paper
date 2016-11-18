package org.bukkit.craftbukkit.block;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.TileEntityMobSpawner;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {
    private final TileEntityMobSpawner spawner;

    public CraftCreatureSpawner(final Block block) {
        super(block);

        spawner = (TileEntityMobSpawner) ((CraftWorld) block.getWorld()).getTileEntityAt(getX(), getY(), getZ());
    }

    public CraftCreatureSpawner(final Material material, TileEntityMobSpawner te) {
        super(material);
        spawner = te;
    }

    public EntityType getSpawnedType() {
        MinecraftKey key = spawner.getSpawner().getMobName();
        return (key == null) ? EntityType.PIG : EntityType.fromName(key.a());
    }

    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        spawner.getSpawner().setMobName(new MinecraftKey(entityType.getName()));
    }

    public String getCreatureTypeName() {
        return spawner.getSpawner().getMobName().a();
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
        return spawner.getSpawner().spawnDelay;
    }

    public void setDelay(int delay) {
        spawner.getSpawner().spawnDelay = delay;
    }

    @Override
    public TileEntityMobSpawner getTileEntity() {
        return spawner;
    }
}
