package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityLargeFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;

public class CraftLargeFireball extends CraftFireball implements LargeFireball {
    public CraftLargeFireball(CraftServer server, EntityLargeFireball entity) {
        super(server, entity);
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        getHandle().yield = (int) yield;
    }

    @Override
    public EntityLargeFireball getHandle() {
        return (EntityLargeFireball) entity;
    }

    @Override
    public String toString() {
        return "CraftLargeFireball";
    }

    public EntityType getType() {
        return EntityType.FIREBALL;
    }
}
