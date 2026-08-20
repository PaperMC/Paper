package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BellRingEvent;
import org.jspecify.annotations.Nullable;

public class CraftBellRingEvent extends CraftBlockEvent implements BellRingEvent {

    private final BlockFace direction;
    private final @Nullable Entity entity;

    private boolean cancelled;

    public CraftBellRingEvent(final Block block, final BlockFace direction, final @Nullable Entity entity) {
        super(block);
        this.direction = direction;
        this.entity = entity;
    }

    @Override
    public BlockFace getDirection() {
        return this.direction;
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return BellRingEvent.getHandlerList();
    }
}
