package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.BreezeWindCharge;

public class CraftBreezeWindCharge extends CraftAbstractWindCharge implements BreezeWindCharge {
    public CraftBreezeWindCharge(CraftServer server, net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge getHandle() {
        return (net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge) this.entity;
    }

    @Override
    public String toString() {
        return "CraftBreezeWindCharge";
    }
}
