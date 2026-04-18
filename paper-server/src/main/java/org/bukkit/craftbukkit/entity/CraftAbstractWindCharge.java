package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AbstractWindCharge;
import org.bukkit.event.entity.EntityRemoveEvent;

public abstract class CraftAbstractWindCharge extends CraftFireball implements AbstractWindCharge {
    public CraftAbstractWindCharge(CraftServer server, net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge getHandle() {
        return (net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge) this.entity;
    }

    @Override
    public void explode() {
        this.getHandle().explode(this.getHandle().position());
        this.getHandle().discard(EntityRemoveEvent.Cause.EXPLODE); // SPIGOT-7577 - explode doesn't discard the entity, this happens only in tick and onHitBlock
    }

    @Override
    public float getExplosionRadius() {
        return this.getYield();
    }

    @Override
    public void setExplosionRadius(float radius) {
        this.setYield(radius);
    }

    @Override
    public boolean isPlayerCreated() {
        return this.getHandle().isPlayerCreated();
    }
}
