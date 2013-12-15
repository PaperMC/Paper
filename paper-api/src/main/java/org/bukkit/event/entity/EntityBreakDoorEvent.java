package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Called when an {@link Entity} breaks a door
 * <p>
 * Cancelling the event will cause the event to be delayed
 */
public class EntityBreakDoorEvent extends EntityChangeBlockEvent {
    public EntityBreakDoorEvent(final LivingEntity entity, final Block targetBlock) {
        super(entity, targetBlock, Material.AIR, (byte) 0);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) entity;
    }
}
