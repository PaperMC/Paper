package org.bukkit.craftbukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.SpongeAbsorbEvent;

public class CraftSpongeAbsorbEvent extends CraftBlockEvent implements SpongeAbsorbEvent {

    private final List<BlockState> blocks;
    private boolean cancelled;

    public CraftSpongeAbsorbEvent(final Block block, final List<BlockState> blocks) {
        super(block);
        this.blocks = blocks;
    }

    @Override
    public List<BlockState> getBlocks() {
        return this.blocks;
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
        return SpongeAbsorbEvent.getHandlerList();
    }
}
