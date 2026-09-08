package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFromToEvent;

public class CraftBlockFromToEvent extends CraftBlockEvent implements BlockFromToEvent {

    protected final Block toBlock;
    protected final BlockFace face;

    protected boolean cancelled;

    public CraftBlockFromToEvent(final Block block, final Block toBlock, final BlockFace face) {
        super(block);
        this.toBlock = toBlock;
        this.face = face;
    }

    public CraftBlockFromToEvent(final Level level, final BlockPos pos, final Direction face) {
        this(CraftBlock.at(level, pos), CraftBlock.at(level, pos.relative(face)), CraftBlock.notchToBlockFace(face));
    }

    public CraftBlockFromToEvent(final Level level, final BlockPos fromPos, final BlockPos toPos) {
        this(CraftBlock.at(level, fromPos), CraftBlock.at(level, toPos), BlockFace.SELF);
    }

    @Override
    public BlockFace getFace() {
        return this.face;
    }

    @Override
    public Block getToBlock() {
        return this.toBlock;
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
        return BlockFromToEvent.getHandlerList();
    }
}
