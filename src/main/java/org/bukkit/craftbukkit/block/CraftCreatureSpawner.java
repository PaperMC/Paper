package org.bukkit.craftbukkit.block;

import net.minecraft.server.TileEntityMobSpawner;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.CreatureType;

public class CraftCreatureSpawner extends CraftBlockState implements CreatureSpawner {
    private final CraftWorld world;
    private final TileEntityMobSpawner spawner;

    public CraftCreatureSpawner(final Block block) {
        super(block);

        world = (CraftWorld)block.getWorld();
        spawner = (TileEntityMobSpawner)world.getTileEntityAt(getX(), getY(), getZ());
    }

    public CreatureType getCreatureType() {
        return CreatureType.fromName(spawner.h);
    }

    public void setCreatureType(CreatureType creatureType) {
        spawner.h = creatureType.getName();
    }

    public String getCreatureTypeId() {
        return spawner.h;
    }

    public void setCreatureTypeId(String creatureType) {
        // Verify input
        CreatureType type = CreatureType.fromName(creatureType);
        if (type == null) {
            return;
        }
        spawner.h = type.getName();
    }
    
    public int getDelay() {
        return spawner.a;
    }

    public void setDelay(int delay) {
        spawner.a = delay;
    }

}
