package org.bukkit.craftbukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftBlockPistonRetractEvent extends CraftBlockPistonEvent implements BlockPistonRetractEvent {

    private final List<Block> blocks;

    public CraftBlockPistonRetractEvent(final Block block, final List<Block> blocks, final BlockFace direction) {
        super(block, direction);
        this.blocks = blocks;
    }

    @Override
    public @Unmodifiable List<Block> getBlocks() {
        return this.blocks;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockPistonRetractEvent.getHandlerList();
    }
}
