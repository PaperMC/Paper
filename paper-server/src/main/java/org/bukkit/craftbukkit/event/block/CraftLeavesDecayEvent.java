package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.LeavesDecayEvent;

public class CraftLeavesDecayEvent extends CraftBlockEvent implements LeavesDecayEvent {

    private boolean cancelled;

    public CraftLeavesDecayEvent(final Level level, final BlockPos pos) {
        super(CraftBlock.at(level, pos));
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
