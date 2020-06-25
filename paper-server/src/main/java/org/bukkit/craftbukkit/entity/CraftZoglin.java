package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityZoglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zoglin;

public class CraftZoglin extends CraftMonster implements Zoglin {

    public CraftZoglin(CraftServer server, EntityZoglin entity) {
        super(server, entity);
    }

    @Override
    public EntityZoglin getHandle() {
        return (EntityZoglin) entity;
    }

    @Override
    public String toString() {
        return "CraftZoglin";
    }

    @Override
    public EntityType getType() {
        return EntityType.ZOGLIN;
    }
}
