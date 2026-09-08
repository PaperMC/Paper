package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.redstone.Redstone;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.checkerframework.common.value.qual.IntRange;

import static io.papermc.paper.util.BoundChecker.requireRange;

public class CraftBlockRedstoneEvent extends CraftBlockEvent implements BlockRedstoneEvent {

    private final int oldCurrent;
    private int newCurrent;

    public CraftBlockRedstoneEvent(final Block block, final int oldCurrent, final int newCurrent) {
        super(block);
        this.oldCurrent = oldCurrent;
        this.newCurrent = newCurrent;
    }

    public CraftBlockRedstoneEvent(final LevelAccessor level, final BlockPos pos, final int oldCurrent, final int newCurrent) {
        this(CraftBlock.at(level, pos), oldCurrent, newCurrent);
    }

    @Override
    public @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int getOldCurrent() {
        return this.oldCurrent;
    }

    @Override
    public @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int getNewCurrent() {
        return this.newCurrent;
    }

    @Override
    public void setNewCurrent(final @IntRange(from = Redstone.SIGNAL_MIN, to = Redstone.SIGNAL_MAX) int newCurrent) {
        this.newCurrent = requireRange(newCurrent, "newCurrent", Redstone.SIGNAL_MIN, Redstone.SIGNAL_MAX);
    }

    @Override
    public HandlerList getHandlers() {
        return BlockRedstoneEvent.getHandlerList();
    }
}
