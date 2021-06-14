package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySkeletonAbstract;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class CraftSkeleton extends CraftAbstractSkeleton implements Skeleton {

    public CraftSkeleton(CraftServer server, EntitySkeleton entity) {
        super(server, entity);
    }

    @Override
    public boolean isConverting() {
        return this.getHandle().fw(); // PAIL rename isStrayConverting
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(this.isConverting(), "Entity is not converting");
        return this.getHandle().conversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            this.getHandle().conversionTime = -1;
            this.getHandle().getDataWatcher().set(EntitySkeleton.DATA_STRAY_CONVERSION_ID, false);
        } else {
            this.getHandle().a(time); // PAIL rename startStrayConversion
        }
    }

    @Override
    public EntitySkeleton getHandle() {
        return (EntitySkeleton) entity;
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
}
