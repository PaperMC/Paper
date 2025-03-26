package org.bukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an {@link Entity} breaks a door
 * <p>
 * Cancelling the event will cause the event to be delayed
 */
public class EntityBreakDoorEvent extends EntityChangeBlockEvent {

    @ApiStatus.Internal
    public EntityBreakDoorEvent(@NotNull final LivingEntity entity, @NotNull final Block targetBlock, @NotNull final org.bukkit.block.data.BlockData to) {
        super(entity, targetBlock, to);
    }

    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }
}
