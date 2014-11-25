package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEndermite;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.EntityType;

public class CraftEndermite extends CraftMonster implements Endermite {

    public CraftEndermite(CraftServer server, EntityEndermite entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftEndermite";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDERMITE;
    }
}
