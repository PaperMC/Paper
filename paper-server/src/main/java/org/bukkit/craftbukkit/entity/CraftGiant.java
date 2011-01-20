package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityZombieSimple;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Giant;

public class CraftGiant extends CraftMonster implements Giant {

    public CraftGiant(CraftServer server, EntityZombieSimple entity) {
        super(server, entity);
    }

}
