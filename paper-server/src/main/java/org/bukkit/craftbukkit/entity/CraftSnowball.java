package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySnowball;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;

public class CraftSnowball extends AbstractProjectile implements Snowball {
    public CraftSnowball(CraftServer server, EntitySnowball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftSnowball";
    }

    public LivingEntity getShooter() {
        if (((EntitySnowball) getHandle()).shooter != null) {
            return (LivingEntity) ((EntitySnowball) getHandle()).shooter.getBukkitEntity();
        }

        return null;
    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftLivingEntity) {
            ((EntitySnowball) getHandle()).shooter = (EntityLiving) ((CraftLivingEntity) shooter).entity;
        }
    }
}
