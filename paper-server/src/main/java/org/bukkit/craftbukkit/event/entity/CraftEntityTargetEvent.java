package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityTargetEvent;
import org.jspecify.annotations.Nullable;

public class CraftEntityTargetEvent extends CraftEntityEvent implements EntityTargetEvent {

    protected @Nullable Entity target;
    private final TargetReason reason;

    private boolean cancelled;

    public CraftEntityTargetEvent(final Entity entity, final @Nullable Entity target, final TargetReason reason) {
        super(entity);
        this.target = target;
        this.reason = reason;
    }

    public CraftEntityTargetEvent(final net.minecraft.world.entity.Entity entity, final net.minecraft.world.entity.@Nullable Entity target, final TargetReason reason) {
        this(entity.getBukkitEntity(), Optionull.map(target, net.minecraft.world.entity.Entity::getBukkitEntity), reason);
    }

    @Override
    public @Nullable Entity getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(final @Nullable Entity target) {
        this.target = target;
    }

    @Override
    public TargetReason getReason() {
        return this.reason;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityTargetEvent.getHandlerList();
    }
}
