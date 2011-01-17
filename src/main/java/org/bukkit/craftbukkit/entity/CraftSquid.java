package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntitySquid;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Squid;

public class CraftSquid extends CraftWaterMob implements Squid {

    public CraftSquid(CraftServer server, EntitySquid entity) {
        super(server, entity);
    }

}
