package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireball;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;

/**
 * A Fireball.
 */
public class CraftFireball extends CraftEntity implements Fireball {
    public CraftFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }

    public float getYield() {
        return ((EntityFireball) getHandle()).yield;
    }

    public boolean isIncendiary() {
        return ((EntityFireball) getHandle()).isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        ((EntityFireball) getHandle()).isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        ((EntityFireball) getHandle()).yield = yield;
    }
}
