package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
import net.minecraft.world.entity.monster.Parched;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Skeleton;
import org.jetbrains.annotations.NotNull;

public class CraftParched extends CraftAbstractSkeleton implements org.bukkit.entity.Parched {
    public CraftParched(final CraftServer server, final Parched entity) {
        super(server, entity);
    }

    @Override
    public Parched getHandle() {
        return (Parched) this.entity;
    }

    @Override
    public Skeleton.@NotNull SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.PARCHED;
    }
}
