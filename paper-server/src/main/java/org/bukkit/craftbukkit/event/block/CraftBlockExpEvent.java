package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;

public class CraftBlockExpEvent extends CraftBlockEvent implements BlockExpEvent {

    private int exp;

    public CraftBlockExpEvent(final Block block, final int exp) {
        super(block);
        this.exp = exp;
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
