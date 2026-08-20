package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.LeavesDecayEvent;

public class CraftLeavesDecayEvent extends CraftBlockEvent implements LeavesDecayEvent {

    private boolean cancelled;

    public CraftLeavesDecayEvent(final Block block) {
        super(block);
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
        return LeavesDecayEvent.getHandlerList();
    }
}
