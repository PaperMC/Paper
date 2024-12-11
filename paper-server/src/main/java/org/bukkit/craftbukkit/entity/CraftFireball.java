package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CraftFireball extends AbstractProjectile implements Fireball {
    public CraftFireball(CraftServer server, AbstractHurtingProjectile entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return this.getHandle().bukkitYield;
    }

    @Override
    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }

    @Override
    public void setYield(float yield) {
        this.getHandle().bukkitYield = yield;
    }

    @Override
    public ProjectileSource getShooter() {
        return this.getHandle().projectileSource;
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        if (shooter instanceof CraftLivingEntity) {
            this.getHandle().setOwner(((CraftLivingEntity) shooter).getHandle());
        } else {
            this.getHandle().setOwner(null);
        }
        this.getHandle().projectileSource = shooter;
    }

    @Override
    public Vector getDirection() {
        return this.getAcceleration();
    }

    @Override
    public void setDirection(Vector direction) {
        Preconditions.checkArgument(direction != null, "Vector direction cannot be null");
        if (direction.isZero()) {
            this.setVelocity(direction);
            this.setAcceleration(direction);
            return;
        }

        direction = direction.clone().normalize();
        this.setVelocity(direction.clone().multiply(this.getVelocity().length()));
        this.setAcceleration(direction.multiply(this.getAcceleration().length()));
    }

    @Override
    public void setAcceleration(@NotNull Vector acceleration) {
        Preconditions.checkArgument(acceleration != null, "Vector acceleration cannot be null");
        // SPIGOT-6993: EntityFireball#assignPower will normalize the given values
        // Note: Because of MC-80142 the fireball will stutter on the client when setting the power to something other than 0 or the normalized vector * 0.1
        this.getHandle().assignDirectionalMovement(new Vec3(acceleration.getX(), acceleration.getY(), acceleration.getZ()), acceleration.length());
        this.update(); // SPIGOT-6579
    }

    @NotNull
    @Override
    public Vector getAcceleration() {
        Vec3 delta = this.getHandle().getDeltaMovement();
        return new Vector(delta.x, delta.y, delta.z);
    }

    @Override
    public AbstractHurtingProjectile getHandle() {
        return (AbstractHurtingProjectile) this.entity;
    }

    @Override
    public String toString() {
        return "CraftFireball";
    }
}
