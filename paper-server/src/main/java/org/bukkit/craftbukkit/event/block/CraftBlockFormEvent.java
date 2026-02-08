package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockFormEvent;

public class CraftBlockFormEvent extends CraftBlockGrowEvent implements BlockFormEvent {

    public CraftBlockFormEvent(final Block block, final BlockState newState) {
        super(block, newState);
    }

    @Override
    public HandlerList getHandlers() {
        return BlockFormEvent.getHandlerList();
    }
}
