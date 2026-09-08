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
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.jetbrains.annotations.Unmodifiable;

public class CraftBlockPistonExtendEvent extends CraftBlockPistonEvent implements BlockPistonExtendEvent {

    private final List<Block> blocks;

    public CraftBlockPistonExtendEvent(final Level level, final BlockPos pos, final List<BlockPos> toPush, final List<BlockPos> toDestroy, final Direction direction) {
        super(level, pos, direction);
        this.blocks = Lists.transform(new CombinedList<>(toPush, toDestroy), p -> CraftBlock.at(level, p));
    }

    @Override
    public @Unmodifiable List<Block> getBlocks() {
        return this.blocks;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockPistonExtendEvent.getHandlerList();
    }
}
