package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Vex;

public class CraftVex extends CraftMonster implements Vex {

    public CraftVex(CraftServer server, net.minecraft.world.entity.monster.Vex entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Vex getHandle() {
        return (net.minecraft.world.entity.monster.Vex) super.getHandle();
    }

    // Paper start
    @Override
    public org.bukkit.entity.Mob getSummoner() {
        net.minecraft.world.entity.Mob owner = getHandle().getOwner();
        return owner != null ? (org.bukkit.entity.Mob) owner.getBukkitEntity() : null;
    }

    @Override
    public void setSummoner(org.bukkit.entity.Mob summoner) {
        getHandle().setOwner(summoner == null ? null : ((CraftMob) summoner).getHandle());
    }

    @Override
    public boolean hasLimitedLifetime() {
        return this.getHandle().hasLimitedLife;
    }

    @Override
    public void setLimitedLifetime(boolean hasLimitedLifetime) {
        this.getHandle().hasLimitedLife = hasLimitedLifetime;
    }

    @Override
    public int getLimitedLifetimeTicks() {
        return this.getHandle().limitedLifeTicks;
    }

    @Override
    public void setLimitedLifetimeTicks(int ticks) {
        this.getHandle().limitedLifeTicks = ticks;
    }
    // Paper end

    @Override
    public String toString() {
        return "CraftVex";
    }

    @Override
    public boolean isCharging() {
        return this.getHandle().isCharging();
    }

    @Override
    public void setCharging(boolean charging) {
        this.getHandle().setIsCharging(charging);
    }

    @Override
    public Location getBound() {
        BlockPos blockPosition = this.getHandle().getBoundOrigin();
        return (blockPosition == null) ? null : CraftLocation.toBukkit(blockPosition, this.getWorld());
    }

    @Override
    public void setBound(Location location) {
        if (location == null) {
            this.getHandle().setBoundOrigin(null);
        } else {
            Preconditions.checkArgument(this.getWorld().equals(location.getWorld()), "The bound world cannot be different to the entity's world.");
            this.getHandle().setBoundOrigin(CraftLocation.toBlockPosition(location));
        }
    }

    @Override
    public int getLifeTicks() {
        return this.getHandle().limitedLifeTicks;
    }

    @Override
    public void setLifeTicks(int lifeTicks) {
        this.getHandle().setLimitedLife(lifeTicks);
        if (lifeTicks < 0) {
            this.getHandle().hasLimitedLife = false;
        }
    }

    @Override
    public boolean hasLimitedLife() {
        return this.getHandle().hasLimitedLife;
    }
}
