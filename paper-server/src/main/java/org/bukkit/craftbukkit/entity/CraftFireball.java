package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }

    public float getYield() {
        return ((EntityFireball) getHandle()).yield;
    }

    public boolean isIncendiary() {
        return ((EntityFireball) getHandle()).isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        ((EntityFireball) getHandle()).isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        ((EntityFireball) getHandle()).yield = yield;
    }

    public LivingEntity getShooter() {
        if (((EntityFireball) getHandle()).shooter != null) {
            return (LivingEntity) ((EntityFireball) getHandle()).shooter.getBukkitEntity();
        }

        return null;

    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftLivingEntity) {
            ((EntityFireball) getHandle()).shooter = (EntityLiving) ((CraftLivingEntity) shooter).entity;
        }
    }

    public Vector getDirection() {
        return new Vector(((EntityFireball) getHandle()).c, ((EntityFireball) getHandle()).d, ((EntityFireball) getHandle()).e);
    }

    public void setDirection(Vector direction) {
        ((EntityFireball) getHandle()).setDirection(direction.getX(), direction.getY(), direction.getZ());
    }
}
