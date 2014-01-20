package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityProjectile;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.server.Entity entity) {
        super(server, entity);
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().shooter = (EntityLiving) ((CraftLivingEntity) shooter).entity;
            if (shooter instanceof CraftHumanEntity) {
                getHandle().shooterName = ((CraftHumanEntity) shooter).getName();
            }
        } else {
            getHandle().shooter = null;
            getHandle().shooterName = null;
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public EntityProjectile getHandle() {
        return (EntityProjectile) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }


    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().shooter == null) {
            return null;
        }
        return (LivingEntity) getHandle().shooter.getBukkitEntity();
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        if (shooter == null) {
            return;
        }
        getHandle().shooter = ((CraftLivingEntity) shooter).getHandle();
        if (shooter instanceof CraftHumanEntity) {
            getHandle().shooterName = ((CraftHumanEntity) shooter).getName();
        }
    }
}
