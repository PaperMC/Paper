package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMobs;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, EntityMobs entity) {
        super(server, entity);
    }

}
