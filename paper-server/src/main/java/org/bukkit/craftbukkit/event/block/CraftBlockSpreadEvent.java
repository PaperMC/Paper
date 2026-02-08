package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockSpreadEvent;

public class CraftBlockSpreadEvent extends CraftBlockFormEvent implements BlockSpreadEvent {

    private final Block source;

    public CraftBlockSpreadEvent(final Block block, final Block source, final BlockState newState) {
        super(block, newState);
        this.source = source;
    }

    @Override
    public Block getSource() {
        return this.source;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockSpreadEvent.getHandlerList();
    }
}
