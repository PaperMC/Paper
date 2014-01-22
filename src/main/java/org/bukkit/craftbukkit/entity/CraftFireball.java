package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.MathHelper;

import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }

    public float getYield() {
        return getHandle().bukkitYield;
    }

    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        getHandle().bukkitYield = yield;
    }

    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().shooter = ((CraftLivingEntity) shooter).getHandle();
        } else {
            getHandle().shooter = null;
        }
        getHandle().projectileSource = shooter;
    }

    public Vector getDirection() {
        return new Vector(getHandle().dirX, getHandle().dirY, getHandle().dirZ);
    }

    public void setDirection(Vector direction) {
        Validate.notNull(direction, "Direction can not be null");
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();
        double magnitude = (double) MathHelper.sqrt(x * x + y * y + z * z);
        getHandle().dirX = x / magnitude;
        getHandle().dirY = y / magnitude;
        getHandle().dirZ = z / magnitude;
    }

    @Override
    public EntityFireball getHandle() {
        return (EntityFireball) entity;
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    @Deprecated
    public void _INVALID_setShooter(LivingEntity shooter) {
        setShooter(shooter);
    }

    @Deprecated
    public LivingEntity _INVALID_getShooter() {
        if (getHandle().shooter != null) {
            return (LivingEntity) getHandle().shooter.getBukkitEntity();
        }
        return null;
    }
}
