package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ghast;

public class CraftGhast extends CraftFlying implements Ghast, CraftEnemy {

    public CraftGhast(CraftServer server, net.minecraft.world.entity.monster.Ghast entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Ghast getHandle() {
        return (net.minecraft.world.entity.monster.Ghast) this.entity;
    }

    @Override
    public String toString() {
        return "CraftGhast";
    }

    @Override
    public boolean isCharging() {
        return this.getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean flag) {
        this.getHandle().setCharging(flag);
    }
}
