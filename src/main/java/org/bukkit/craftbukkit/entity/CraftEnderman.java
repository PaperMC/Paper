package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEnderman;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Enderman;

public class CraftEnderman extends CraftMonster implements Enderman {
    public CraftEnderman(CraftServer server, EntityEnderman entity) {
        super(server, entity);
    }

    @Override
    public EntityEnderman getHandle() {
        return (EntityEnderman) super.getHandle();
    }
}
