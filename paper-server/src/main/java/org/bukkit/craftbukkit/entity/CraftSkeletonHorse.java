package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.SkeletonHorse;

public class CraftSkeletonHorse extends CraftAbstractHorse implements SkeletonHorse {

    public CraftSkeletonHorse(CraftServer server, net.minecraft.world.entity.animal.horse.SkeletonHorse entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.horse.SkeletonHorse getHandle() {
        return (net.minecraft.world.entity.animal.horse.SkeletonHorse) this.entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.SKELETON_HORSE;
    }

    @Override
    public boolean isTrapped() {
        return this.getHandle().isTrap();
    }

    @Override
    public void setTrapped(boolean trapped) {
        this.getHandle().setTrap(trapped);
    }

    @Override
    public int getTrapTime() {
        return this.getHandle().trapTime;
    }

    @Override
    public void setTrapTime(int trapTime) {
        this.getHandle().trapTime = trapTime;
    }
}
