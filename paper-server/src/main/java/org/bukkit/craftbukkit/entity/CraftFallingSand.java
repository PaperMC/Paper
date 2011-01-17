package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFallingSand;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand extends CraftEntity implements FallingSand {

    public CraftFallingSand(CraftServer server, EntityFallingSand entity) {
        super(server, entity);
    }

}
