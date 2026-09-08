package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBurnEvent;

public class CraftBlockBurnEvent extends CraftBlockEvent implements BlockBurnEvent {

    private final Block ignitingBlock;
    private boolean cancelled;

    public CraftBlockBurnEvent(final Block block, final Block ignitingBlock) {
        super(block);
        this.ignitingBlock = ignitingBlock;
    }

    public CraftBlockBurnEvent(final Level level, final BlockPos pos, final BlockPos sourcePos) {
        this(CraftBlock.at(level, pos), CraftBlock.at(level, sourcePos));
    }

    @Override
    public Block getIgnitingBlock() {
        return this.ignitingBlock;
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
        return BlockBurnEvent.getHandlerList();
    }
}
