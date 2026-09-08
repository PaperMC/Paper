package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;

public class CraftBlockExpEvent extends CraftBlockEvent implements BlockExpEvent {

    private int exp;

    public CraftBlockExpEvent(final Block block, final int exp) {
        super(block);
        this.exp = exp;
    }

    public CraftBlockExpEvent(final Level level, final BlockPos pos, final int exp) {
        this(CraftBlock.at(level, pos), exp);
    }

    @Override
    public int getExpToDrop() {
        return this.exp;
    }

    @Override
    public void setExpToDrop(final int exp) {
        this.exp = exp;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockExpEvent.getHandlerList();
    }
}
