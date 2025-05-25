package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LargeFireball;

public class CraftLargeFireball extends CraftSizedFireball implements LargeFireball {

    public CraftLargeFireball(CraftServer server, net.minecraft.world.entity.projectile.LargeFireball entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.LargeFireball getHandle() {
        return (net.minecraft.world.entity.projectile.LargeFireball) this.entity;
    }

    @Override
    public void setYield(float yield) {
        super.setYield(yield);
        this.getHandle().explosionPower = (int) yield;
    }
}
