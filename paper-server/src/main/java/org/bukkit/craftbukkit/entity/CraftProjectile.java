package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.LivingEntity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.world.entity.projectile.Projectile entity) {
        super(server, entity);
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().setOwner((LivingEntity) ((CraftLivingEntity) shooter).entity);
        } else {
            this.getHandle().setOwner(null);
        }
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public net.minecraft.world.entity.projectile.Projectile getHandle() {
        return (net.minecraft.world.entity.projectile.Projectile) this.entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
