package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.block.CraftBlockEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class PaperBlockBreakProgressUpdateEvent extends CraftBlockEvent implements BlockBreakProgressUpdateEvent {

    private final float progress;
    private final Entity entity;

    public PaperBlockBreakProgressUpdateEvent(final Block block, final float progress, final Entity entity) {
        super(block);
        this.progress = progress;
        this.entity = entity;
    }

    @Override
    public float getProgress() {
        return this.progress;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockBreakProgressUpdateEvent.getHandlerList();
    }
}
