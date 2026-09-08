package org.bukkit.craftbukkit.event.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class CraftEntityChangeBlockEvent extends CraftEntityEvent implements EntityChangeBlockEvent {

    private final Block block;
    private final BlockData to;

    private boolean cancelled;

    public CraftEntityChangeBlockEvent(final Entity entity, final Block block, final BlockData to) {
        super(entity);
        this.block = block;
        this.to = to;
    }

    public CraftEntityChangeBlockEvent(final net.minecraft.world.entity.Entity entity, final BlockPos pos, final BlockState to) {
        this(entity.getBukkitEntity(), CraftBlock.at(entity.level(), pos), to.asBlockData());
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public Material getTo() {
        return this.to.getMaterial();
    }

    @Override
    public BlockData getBlockData() {
        return this.to.clone();
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
        return EntityChangeBlockEvent.getHandlerList();
    }
}
