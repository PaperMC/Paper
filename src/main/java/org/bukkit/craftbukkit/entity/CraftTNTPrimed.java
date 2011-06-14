package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityTNTPrimed;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, EntityTNTPrimed entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    public float getYield() {
        return ((EntityTNTPrimed) getHandle()).yield;
    }

    public boolean isIncendiary() {
        return ((EntityTNTPrimed) getHandle()).isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        ((EntityTNTPrimed) getHandle()).isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        ((EntityTNTPrimed) getHandle()).yield = yield;
    }

    public int getFuseTicks() {
        return ((EntityTNTPrimed) getHandle()).a;
    }

    public void setFuseTicks(int fuseTicks) {
        ((EntityTNTPrimed) getHandle()).a = fuseTicks;
    }

}
