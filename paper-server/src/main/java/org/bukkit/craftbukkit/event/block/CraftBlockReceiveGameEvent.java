package org.bukkit.craftbukkit.event.block;

import org.bukkit.GameEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockReceiveGameEvent extends CraftBlockEvent implements BlockReceiveGameEvent {

    private final GameEvent event;
    private final @Nullable Entity entity;

    private boolean cancelled;

    public CraftBlockReceiveGameEvent(final GameEvent event, final Block block, final @Nullable Entity entity) {
        super(block);
        this.event = event;
        this.entity = entity;
    }

    @Override
    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
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
        return BlockReceiveGameEvent.getHandlerList();
    }
}
