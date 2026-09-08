package org.bukkit.craftbukkit.event.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityBreakDoorEvent;

public class CraftEntityBreakDoorEvent extends CraftEntityChangeBlockEvent implements EntityBreakDoorEvent {

    public CraftEntityBreakDoorEvent(final LivingEntity entity, final Block targetBlock, final BlockData to) {
        super(entity, targetBlock, to);
    }

    public CraftEntityBreakDoorEvent(final net.minecraft.world.entity.LivingEntity entity, final BlockPos pos, final BlockState to) {
        this(entity.getBukkitEntity(), CraftBlock.at(entity.level(), pos), to.asBlockData());
    }

    @Override
    public LivingEntity getEntity() {
        return (LivingEntity) this.entity;
    }
}
