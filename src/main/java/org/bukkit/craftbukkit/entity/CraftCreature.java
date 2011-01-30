package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityCreature;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;

public class CraftCreature extends CraftLivingEntity implements Creature{

    public CraftCreature(CraftServer server, EntityCreature entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }

}
