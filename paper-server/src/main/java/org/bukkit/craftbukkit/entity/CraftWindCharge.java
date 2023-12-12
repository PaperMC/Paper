package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WindCharge;

public class CraftWindCharge extends CraftFireball implements WindCharge {
    public CraftWindCharge(CraftServer server, net.minecraft.world.entity.projectile.WindCharge entity) {
        super(server, entity);
    }

    @Override
    public void explode() {
        this.getHandle().explode();
    }

    @Override
    public net.minecraft.world.entity.projectile.WindCharge getHandle() {
        return (net.minecraft.world.entity.projectile.WindCharge) this.entity;
    }

    @Override
    public String toString() {
        return "CraftWindCharge";
    }
}
