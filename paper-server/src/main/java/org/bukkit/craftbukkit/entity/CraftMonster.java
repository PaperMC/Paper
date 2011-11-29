package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityMonster;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, EntityMonster entity) {
        super(server, entity);
    }

    @Override
    public EntityMonster getHandle() {
        return (EntityMonster) entity;
    }

    @Override
    public String toString() {
        return "CraftMonster";
    }
}
