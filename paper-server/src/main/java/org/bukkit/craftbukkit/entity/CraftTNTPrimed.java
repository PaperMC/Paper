package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityTNTPrimed;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, EntityTNTPrimed entity) {
        super(server, entity);
    }

}
