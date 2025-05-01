package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.WitherSkeleton;

public class CraftWitherSkeleton extends CraftAbstractSkeleton implements WitherSkeleton {

    public CraftWitherSkeleton(CraftServer server, net.minecraft.world.entity.monster.WitherSkeleton entity) {
        super(server, entity);
    }

    @Override
    public SkeletonType getSkeletonType() {
        return SkeletonType.WITHER;
    }
}
