package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.EntityFireball;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, EntityFireball entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return getHandle().bukkitYield;
    }

    @Override
    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    @Override
    public void setYield(float yield) {
        getHandle().bukkitYield = yield;
    }

    @Override
    public ProjectileSource getShooter() {
        return getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().setOwner(((CraftLivingEntity) shooter).getHandle());
        } else {
            getHandle().setOwner(null);
        }
        getHandle().projectileSource = shooter;
    }

    @Override
    public Vector getDirection() {
        return new Vector(getHandle().xPower, getHandle().yPower, getHandle().zPower);
    }

    @Override
    public void setDirection(Vector direction) {
        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        if (direction.isZero()) {
            setVelocity(direction);
            return;
        }
        getHandle().assignPower(direction.getX(), direction.getY(), direction.getZ());
        update(); // SPIGOT-6579
    }

    @Override
    public void setVelocity(Vector velocity) {
        Preconditions.checkArgument(velocity != null, "Vector velocity cannot be null");
        // SPIGOT-6993: Allow power to be higher / lower than the normalized direction enforced by #setDirection(Vector)
        // Note: Because of MC-80142 the fireball will stutter on the client when setting the velocity to something other than 0 or the normalized vector * 0.1
        getHandle().xPower = velocity.getX();
        getHandle().yPower = velocity.getY();
        getHandle().zPower = velocity.getZ();
        update(); // SPIGOT-6579
    }

    @Override
    public EntityFireball getHandle() {
        return (EntityFireball) entity;
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }
}
