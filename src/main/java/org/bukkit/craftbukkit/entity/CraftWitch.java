package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.entity.CraftRangedEntity; // Paper
import net.minecraft.server.EntityWitch;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftRaider implements Witch, CraftRangedEntity<EntityWitch> { // Paper
    public CraftWitch(CraftServer server, EntityWitch entity) {
        super(server, entity);
    }

    @Override
    public EntityWitch getHandle() {
        return (EntityWitch) entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITCH;
    }
}
