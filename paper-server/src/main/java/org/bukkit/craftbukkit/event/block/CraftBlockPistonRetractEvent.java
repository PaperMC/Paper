package org.bukkit.craftbukkit.event.block;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.util.CombinedList;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftBlockPistonRetractEvent extends CraftBlockPistonEvent implements BlockPistonRetractEvent {

    private final List<Block> blocks;

    public CraftBlockPistonRetractEvent(final Level level, final BlockPos pos, final List<Block> blocks, final Direction direction) {
        super(level, pos, direction);
        this.blocks = blocks;
    }

    public CraftBlockPistonRetractEvent(final Level level, final BlockPos pos, final List<BlockPos> toPush, final List<BlockPos> toDestroy, final Direction direction) {
        super(level, pos, direction);
        this.blocks = Lists.transform(new CombinedList<>(toPush, toDestroy), p -> CraftBlock.at(level, p));
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
