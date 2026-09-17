package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.jetbrains.annotations.Nullable;

public class CraftMinecartTNT extends CraftMinecart implements ExplosiveMinecart {

    public CraftMinecartTNT(CraftServer server, MinecartTNT entity) {
        super(server, entity);
    }

    @Override
    public MinecartTNT getHandle() {
        return (MinecartTNT) this.entity;
    }

    @Override
    public float getYield() {
        return this.getHandle().explosionPowerBase;
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
        this.getHandle().explosionPowerBase = yield;
    }

    @Override
    public float getExplosionSpeedFactor() {
        return this.getHandle().explosionSpeedFactor;
    }

    @Override
    public void setExplosionSpeedFactor(float factor) {
        this.getHandle().explosionSpeedFactor = factor;
    }

    @Override
    public void setFuseTicks(int ticks) {
        this.getHandle().fuse = ticks;
    }

    @Override
    public int getFuseTicks() {
        return this.getHandle().getFuse();
    }

    @Override
    public void ignite() {
        this.getHandle().primeFuse(null);
    }

    @Override
    public void ignite(int fuseTime) {
        Preconditions.checkArgument(fuseTime >= 0, "Fuse time must be greater than or equal to 0 (got %s)", fuseTime);

        this.getHandle().primeFuse(null, fuseTime);
    }

    @Override
    public void ignite(@Nullable Entity igniter) {
        // primeFuse() only uses the DamageSource's source entity to generate its actual explosion source.
        DamageSource damageSource = igniter == null ? null
            : this.getHandle().damageSources().explosion(this.getHandle(), ((CraftEntity) igniter).getHandle());
        this.getHandle().primeFuse(damageSource);
    }

    @Override
    public void ignite(@Nullable Entity igniter, int fuseTime) {
        Preconditions.checkArgument(fuseTime >= 0, "Fuse time must be greater than or equal to 0 (got %s)", fuseTime);

        // primeFuse() only uses the DamageSource's source entity to generate its actual explosion source.
        DamageSource damageSource = igniter == null ? null
            : this.getHandle().damageSources().explosion(this.getHandle(), ((CraftEntity) igniter).getHandle());
        this.getHandle().primeFuse(damageSource, fuseTime);
    }

    @Override
    public boolean isIgnited() {
        return this.getHandle().isPrimed();
    }

    @Override
    public @Nullable Entity getIgniter() {
        if (this.getHandle().ignitionSource != null && this.getHandle().ignitionSource.getEntity() != null) {
            return this.getHandle().ignitionSource.getEntity().getBukkitEntity();
        }

        return null;
    }

    @Override
    public void explode() {
        this.explode(null);
    }

    @Override
    public void explode(double power) {
        this.explode(null, power);
    }

    @Override
    public void explode(@Nullable Entity entity) {
        this.explode(entity, this.getHandle().getDeltaMovement().horizontalDistanceSqr());
    }

    @Override
    public void explode(@Nullable Entity entity, double power) {
        Preconditions.checkArgument(0 <= power && power <= 5 * 5, "Power must be in range [0, 25] (got %s)", power);

        // Mirrors what MinecartTNT sets its futurely-used `DamageSource ignitionSource` to when primed by an entity
        DamageSource damageSource = entity == null ? null
            : this.getHandle().damageSources().explosion(this.getHandle(), ((CraftEntity) entity).getHandle());
        this.getHandle().explode(damageSource, power);
    }
}
