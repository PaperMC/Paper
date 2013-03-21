package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityTNTPrimed;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, EntityTNTPrimed entity) {
        super(server, entity);
    }

    public float getYield() {
        return getHandle().yield;
    }

    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        getHandle().yield = yield;
    }

    public int getFuseTicks() {
        return getHandle().fuseTicks;
    }

    public void setFuseTicks(int fuseTicks) {
        getHandle().fuseTicks = fuseTicks;
    }

    @Override
    public EntityTNTPrimed getHandle() {
        return (EntityTNTPrimed) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    public Entity getSource() {
        EntityLiving source = getHandle().getSource();

        if (source != null) {
            Entity bukkitEntity = source.getBukkitEntity();

            if (bukkitEntity.isValid()) {
                return bukkitEntity;
            }
        }

        return null;
    }
}
