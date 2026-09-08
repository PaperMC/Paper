package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BellRingEvent;
import org.jspecify.annotations.Nullable;

public class CraftBellRingEvent extends CraftBlockEvent implements BellRingEvent {

    private final BlockFace face;
    private final @Nullable Entity entity;

    private boolean cancelled;

    public CraftBellRingEvent(final Block block, final BlockFace face, final @Nullable Entity entity) {
        super(block);
        this.face = face;
        this.entity = entity;
    }

    @Override
    public BlockFace getDirection() {
        return this.face;
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
