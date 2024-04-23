package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AbstractWindCharge;
import org.bukkit.event.entity.EntityRemoveEvent;

public abstract class CraftAbstractWindCharge extends CraftFireball implements AbstractWindCharge {
    public CraftAbstractWindCharge(CraftServer server, net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge entity) {
        super(server, entity);
    }

    @Override
    public void explode() {
        this.getHandle().explode();
        this.getHandle().discard(EntityRemoveEvent.Cause.EXPLODE); // SPIGOT-7577 - explode doesn't discard the entity, this happens only in tick and onHitBlock
    }

    @Override
    public net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge getHandle() {
        return (net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge) this.entity;
    }

    @Override
    public String toString() {
        return "CraftAbstractWindCharge";
    }
}
