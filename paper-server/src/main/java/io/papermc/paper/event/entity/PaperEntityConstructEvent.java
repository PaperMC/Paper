package io.papermc.paper.event.entity;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Unmodifiable;

public class PaperEntityConstructEvent extends CraftEntityEvent implements EntityConstructEvent {

    private final List<Block> blocks;
    private boolean cancelled;

    public PaperEntityConstructEvent(final Entity entity, final List<Block> blocks) {
        super(entity);
        this.blocks = List.copyOf(blocks);
    }

    @Override
    public @Unmodifiable List<Block> getBlocks() {
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
        return EntityConstructEvent.getHandlerList();
    }
}
