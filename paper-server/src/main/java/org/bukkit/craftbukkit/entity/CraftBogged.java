package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Bogged;
import org.bukkit.entity.Skeleton;

public class CraftBogged extends CraftAbstractSkeleton implements Bogged {

    public CraftBogged(CraftServer server, net.minecraft.world.entity.monster.Bogged entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Bogged getHandle() {
        return (net.minecraft.world.entity.monster.Bogged) entity;
    }

    @Override
    public String toString() {
        return "CraftBogged";
    }

    @Override
    public Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.BOGGED;
    }
}
