package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.EntityRavager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ravager;

public class CraftRavager extends CraftRaider implements Ravager {

    public CraftRavager(CraftServer server, EntityRavager entity) {
        super(server, entity);
    }

    @Override
    public EntityRavager getHandle() {
        return (EntityRavager) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftRavager";
    }
}
