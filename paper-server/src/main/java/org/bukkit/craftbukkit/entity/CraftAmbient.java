package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityAmbient;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;

public class CraftAmbient extends CraftLivingEntity implements Ambient {
    public CraftAmbient(CraftServer server, EntityAmbient entity) {
        super(server, entity);
    }

    @Override
    public EntityAmbient getHandle() {
        return (EntityAmbient) entity;
    }

    @Override
    public String toString() {
        return "CraftAmbient";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
