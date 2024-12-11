package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import org.bukkit.craftbukkit.CraftServer;

public class CraftBlockAttachedEntity extends CraftEntity {
    public CraftBlockAttachedEntity(CraftServer server, BlockAttachedEntity entity) {
        super(server, entity);
    }

    @Override
    public BlockAttachedEntity getHandle() {
        return (BlockAttachedEntity) this.entity;
    }

    @Override
    public String toString() {
        return "CraftBlockAttachedEntity";
    }
}
