package org.bukkit.craftbukkit.event.entity;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityBreakDoorEvent;

public class CraftEntityBreakDoorEvent extends CraftEntityChangeBlockEvent implements EntityBreakDoorEvent {

    public CraftEntityBreakDoorEvent(final LivingEntity entity, final Block targetBlock, final BlockData to) {
        super(entity, targetBlock, to);
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }
}
