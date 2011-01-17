package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityWaterMob;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WaterMob;

public class CraftWaterMob extends CraftCreature implements WaterMob {

    public CraftWaterMob(CraftServer server, EntityWaterMob entity) {
        super(server, entity);
    }

}
