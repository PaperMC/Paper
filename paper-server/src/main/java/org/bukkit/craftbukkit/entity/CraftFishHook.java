package org.bukkit.craftbukkit.entity;

import net.minecraft.core.BlockPosition;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.projectile.EntityFishingHook;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.FishHook.HookState;

public class CraftFishHook extends CraftProjectile implements FishHook {
    private double biteChance = -1;

    public CraftFishHook(CraftServer server, EntityFishingHook entity) {
        super(server, entity);
    }

    @Override
    public EntityFishingHook getHandle() {
        return (EntityFishingHook) entity;
    }

    @Override
    public String toString() {
        return "CraftFishingHook";
    }

    @Override
    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }

    @Override
    public int getMinWaitTime() {
        return getHandle().minWaitTime;
    }

    @Override
    public void setMinWaitTime(int minWaitTime) {
        EntityFishingHook hook = getHandle();
        Validate.isTrue(minWaitTime >= 0 && minWaitTime <= this.getMaxWaitTime(), "The minimum wait time should be between 0 and the maximum wait time.");
        hook.minWaitTime = minWaitTime;
    }

    @Override
    public int getMaxWaitTime() {
        return getHandle().maxWaitTime;
    }

    @Override
    public void setMaxWaitTime(int maxWaitTime) {
        EntityFishingHook hook = getHandle();
        Validate.isTrue(maxWaitTime >= 0 && maxWaitTime >= this.getMinWaitTime(), "The maximum wait time should be higher than or equal to 0 and the minimum wait time.");
        hook.maxWaitTime = maxWaitTime;
    }

    @Override
    public boolean getApplyLure() {
        return getHandle().applyLure;
    }

    @Override
    public void setApplyLure(boolean applyLure) {
        getHandle().applyLure = applyLure;
    }

    @Override
    public double getBiteChance() {
        EntityFishingHook hook = getHandle();

        if (this.biteChance == -1) {
            if (hook.level.isRainingAt(new BlockPosition(MathHelper.floor(hook.locX()), MathHelper.floor(hook.locY()) + 1, MathHelper.floor(hook.locZ())))) {
                return 1 / 300.0;
            }
            return 1 / 500.0;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double chance) {
        Validate.isTrue(chance >= 0 && chance <= 1, "The bite chance must be between 0 and 1.");
        this.biteChance = chance;
    }

    @Override
    public boolean isInOpenWater() {
        return getHandle().isInOpenWater();
    }

    @Override
    public Entity getHookedEntity() {
        net.minecraft.world.entity.Entity hooked = getHandle().hookedIn;
        return (hooked != null) ? hooked.getBukkitEntity() : null;
    }

    @Override
    public void setHookedEntity(Entity entity) {
        EntityFishingHook hook = getHandle();

        hook.hookedIn = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        hook.getDataWatcher().set(EntityFishingHook.DATA_HOOKED_ENTITY, hook.hookedIn != null ? hook.hookedIn.getId() + 1 : 0);
    }

    @Override
    public boolean pullHookedEntity() {
        EntityFishingHook hook = getHandle();
        if (hook.hookedIn == null) {
            return false;
        }

        hook.reel(hook.hookedIn);
        return true;
    }

    @Override
    public HookState getState() {
        return HookState.values()[getHandle().currentState.ordinal()];
    }
}
