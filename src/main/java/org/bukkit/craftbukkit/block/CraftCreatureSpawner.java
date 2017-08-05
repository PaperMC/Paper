package org.bukkit.craftbukkit.block;

import net.minecraft.server.MinecraftKey;
import net.minecraft.server.TileEntityMobSpawner;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class CraftCreatureSpawner extends CraftBlockEntityState<TileEntityMobSpawner> implements CreatureSpawner {

    public CraftCreatureSpawner(final Block block) {
        super(block, TileEntityMobSpawner.class);
    }

    public CraftCreatureSpawner(final Material material, TileEntityMobSpawner te) {
        super(material, te);
    }

    @Override
    public EntityType getSpawnedType() {
        MinecraftKey key = this.getSnapshot().getSpawner().getMobName();
        return (key == null) ? EntityType.PIG : EntityType.fromName(key.getKey());
    }

    @Override
    public void setSpawnedType(EntityType entityType) {
        if (entityType == null || entityType.getName() == null) {
            throw new IllegalArgumentException("Can't spawn EntityType " + entityType + " from mobspawners!");
        }

        this.getSnapshot().getSpawner().setMobName(new MinecraftKey(entityType.getName()));
    }

    @Override
    public String getCreatureTypeName() {
        return this.getSnapshot().getSpawner().getMobName().getKey();
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
}
