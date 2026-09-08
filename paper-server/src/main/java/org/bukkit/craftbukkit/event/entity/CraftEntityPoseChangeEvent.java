package org.bukkit.craftbukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityPoseChangeEvent;

public class CraftEntityPoseChangeEvent extends CraftEntityEvent implements EntityPoseChangeEvent {

    private final Pose pose;

    public CraftEntityPoseChangeEvent(final Entity entity, final Pose pose) {
        super(entity);
        this.pose = pose;
    }

    public CraftEntityPoseChangeEvent(final net.minecraft.world.entity.Entity entity, final net.minecraft.world.entity.Pose pose) {
        this(entity.getBukkitEntity(), Pose.values()[pose.ordinal()]);
    }

    @Override
    public Pose getPose() {
        return this.pose;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityPoseChangeEvent.getHandlerList();
    }
}
