package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireworks;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;

public class CraftFirework extends CraftEntity implements Firework {

    public CraftFirework(CraftServer server, EntityFireworks entity) {
        super(server, entity);
    }

    @Override
    public EntityFireworks getHandle() {
        return (EntityFireworks) entity;
    }

    @Override
    public String toString() {
        return "CraftFirework";
    }

    public EntityType getType() {
        return EntityType.FIREWORK;
    }
}
