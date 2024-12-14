package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an {@link Entity} breaks a door
 * <p>
 * Cancelling the event will cause the event to be delayed
 *
 * @since 1.2.5 R0.1
 */
public class EntityBreakDoorEvent extends EntityChangeBlockEvent {
    public EntityBreakDoorEvent(@NotNull final LivingEntity entity, @NotNull final Block targetBlock, @NotNull final org.bukkit.block.data.BlockData to) { // Paper
        super(entity, targetBlock, to); // Paper
    }

    /**
     * @since 1.4.5 R1.0
     */
    @NotNull
    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }
}
