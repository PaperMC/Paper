package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.entity.CraftRangedEntity;
import net.minecraft.server.EntitySkeletonAbstract;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class CraftSkeleton extends CraftMonster implements Skeleton, CraftRangedEntity<EntitySkeletonAbstract> { // Paper

    public CraftSkeleton(CraftServer server, EntitySkeletonAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntitySkeletonAbstract getHandle() {
        return (EntitySkeletonAbstract) entity;
    }

    @Override
    public String toString() {
        return "CraftSkeleton";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON;
    }

    @Override
    public SkeletonType getSkeletonType() {
       return SkeletonType.NORMAL;
    }

    @Override
    public void setSkeletonType(SkeletonType type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
