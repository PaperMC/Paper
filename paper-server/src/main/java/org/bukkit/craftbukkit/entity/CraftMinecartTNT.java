package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.minecart.ExplosiveMinecart;

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
    public boolean isIgnited() {
        return this.getHandle().isPrimed();
    }

    @Override
    public void explode() {
        this.getHandle().explode(null, this.getHandle().getDeltaMovement().horizontalDistanceSqr());
    }

    @Override
    public void explode(double power) {
        Preconditions.checkArgument(0 <= power && power <= Mth.square(5), "Power must be in range [0, 25] (got %s)", power);

        this.getHandle().explode(null, power);
    }
}
