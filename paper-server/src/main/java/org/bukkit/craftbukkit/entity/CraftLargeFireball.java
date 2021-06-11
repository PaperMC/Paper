package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.projectile.EntityLargeFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;

public class CraftLargeFireball extends CraftSizedFireball implements LargeFireball {
    public CraftLargeFireball(CraftServer server, EntityLargeFireball entity) {
        super(server, entity);
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        getHandle().explosionPower = (int) yield;
    }

    @Override
    public EntityLargeFireball getHandle() {
        return (EntityLargeFireball) entity;
    }

    @Override
    public String toString() {
        return "CraftLargeFireball";
    }

    @Override
    public EntityType getType() {
        return EntityType.FIREBALL;
    }
}
