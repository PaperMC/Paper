package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityReference;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vex;
import org.jetbrains.annotations.Nullable;

public class CraftVex extends CraftMonster implements Vex {

    public CraftVex(CraftServer server, net.minecraft.world.entity.monster.Vex entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Vex getHandle() {
        return (net.minecraft.world.entity.monster.Vex) this.entity;
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        return Optionull.map(this.getHandle().getOwner(), net.minecraft.world.entity.LivingEntity::getBukkitLivingEntity);
    }

    @Override
    public void setOwner(final @Nullable LivingEntity owner) {
        this.getHandle().owner = owner == null ? null : EntityReference.of(((CraftLivingEntity) owner).getHandle());
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
        BlockPos pos = this.getHandle().getBoundOrigin();
        return (pos == null) ? null : CraftLocation.toBukkit(pos, this.getWorld());
    }

    @Override
    public void setBound(Location location) {
        if (location == null) {
            this.getHandle().setBoundOrigin(null);
        } else {
            Preconditions.checkArgument(this.getWorld().equals(location.getWorld()), "The bound world cannot be different to the entity's world.");
            this.getHandle().setBoundOrigin(CraftLocation.toBlockPos(location));
        }
    }

    @Override
    public void setLifeTicks(int lifeTicks) {
        this.getHandle().setLimitedLife(lifeTicks);
        if (lifeTicks < 0) {
            this.getHandle().hasLimitedLife = false;
        }
    }
}
