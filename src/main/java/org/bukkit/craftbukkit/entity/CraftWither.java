package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWither;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Wither;
import org.bukkit.entity.EntityType;

public class CraftWither extends CraftMonster implements Wither {
    public CraftWither(CraftServer server, EntityWither entity) {
        super(server, entity);
    }

    @Override
    public EntityWither getHandle() {
        return (EntityWither) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    public EntityType getType() {
        return EntityType.WITHER;
    }
}
