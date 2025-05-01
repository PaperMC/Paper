package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Bogged;
import org.bukkit.entity.Skeleton;

public class CraftBogged extends CraftAbstractSkeleton implements Bogged, io.papermc.paper.entity.PaperShearable { // Paper - Shear API

    public CraftBogged(CraftServer server, net.minecraft.world.entity.monster.Bogged entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Bogged getHandle() {
        return (net.minecraft.world.entity.monster.Bogged) this.entity;
    }

    @Override
    public Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.BOGGED;
    }

    @Override
    public boolean isSheared() {
        return this.getHandle().isSheared();
    }

    @Override
    public void setSheared(boolean flag) {
        this.getHandle().setSheared(flag);
    }
}
