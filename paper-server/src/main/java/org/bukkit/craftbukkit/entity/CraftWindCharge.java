package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.WindCharge;

public class CraftWindCharge extends CraftAbstractWindCharge implements WindCharge {
    public CraftWindCharge(CraftServer server, net.minecraft.world.entity.projectile.windcharge.WindCharge entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.windcharge.WindCharge getHandle() {
        return (net.minecraft.world.entity.projectile.windcharge.WindCharge) this.entity;
    }

    @Override
    public String toString() {
        return "CraftWindCharge";
    }
}
