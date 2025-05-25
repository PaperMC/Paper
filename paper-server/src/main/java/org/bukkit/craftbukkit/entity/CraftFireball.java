package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftVector;
import org.bukkit.entity.Fireball;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class CraftFireball extends AbstractProjectile implements Fireball {

    public CraftFireball(CraftServer server, AbstractHurtingProjectile entity) {
        super(server, entity);
    }

    @Override
    public AbstractHurtingProjectile getHandle() {
        return (AbstractHurtingProjectile) this.entity;
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
        // SPIGOT-6993: AbstractHurtingProjectile#assignDirectionalMovement will normalize the given values
        // Note: Because of MC-80142 the fireball will stutter on the client when setting the power to something other than 0 or the normalized vector * 0.1
        this.getHandle().assignDirectionalMovement(CraftVector.toVec3(acceleration), acceleration.length());
        this.update(); // SPIGOT-6579
    }

    @NotNull
    @Override
    public Vector getAcceleration() {
        return CraftVector.toBukkit(this.getHandle().getDeltaMovement());
    }

    // Paper start - Expose power on fireball projectiles
    @Override
    public void setPower(final Vector power) {
        this.setAcceleration(power);
    }

    @Override
    public Vector getPower() {
        return this.getAcceleration();
    }
    // Paper end - Expose power on fireball projectiles
}
