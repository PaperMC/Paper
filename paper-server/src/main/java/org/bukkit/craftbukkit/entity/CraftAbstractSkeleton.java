package org.bukkit.craftbukkit.entity;

import net.kyori.adventure.util.TriState;
import net.minecraft.tags.EntityTypeTags;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.Skeleton;

public abstract class CraftAbstractSkeleton extends CraftMonster implements AbstractSkeleton, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.skeleton.AbstractSkeleton> { // Paper

    public CraftAbstractSkeleton(CraftServer server, net.minecraft.world.entity.monster.skeleton.AbstractSkeleton entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.skeleton.AbstractSkeleton getHandle() {
        return (net.minecraft.world.entity.monster.skeleton.AbstractSkeleton) this.entity;
    }

    @Override
    public void setSkeletonType(Skeleton.SkeletonType type) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean shouldBurnInDay() {
        return super.burnsInDaylight();
    }

    @Override
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        super.setBurnInDaylightOverride(shouldBurnInDay == this.getHandle().getType().is(EntityTypeTags.BURN_IN_DAYLIGHT) ? TriState.NOT_SET : TriState.FALSE); // Use NOT_SET if the default value is set with this
    }
}
