package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperEntityInsideBlockEvent extends CraftEntityEvent implements EntityInsideBlockEvent {

    private final Block block;
    private boolean cancelled;

    public PaperEntityInsideBlockEvent(final Entity entity, final Block block) {
        super(entity);
        this.block = block;
    }

    @Override
    public Block getBlock() {
        return this.block;
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
        return EntityInsideBlockEvent.getHandlerList();
    }
}
