package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockIgniteEvent;
import org.jspecify.annotations.Nullable;

public class CraftBlockIgniteEvent extends CraftBlockEvent implements BlockIgniteEvent {

    private final IgniteCause cause;
    private final @Nullable Entity ignitingEntity;
    private final @Nullable Block ignitingBlock;

    private boolean cancelled;

    public CraftBlockIgniteEvent(final Block block, final IgniteCause cause, final @Nullable Entity ignitingEntity) {
        this(block, cause, ignitingEntity, null);
    }

    public CraftBlockIgniteEvent(final Block block, final IgniteCause cause, final @Nullable Block ignitingBlock) {
        this(block, cause, null, ignitingBlock);
    }

    public CraftBlockIgniteEvent(final Block block, final IgniteCause cause, final @Nullable Entity ignitingEntity, final @Nullable Block ignitingBlock) {
        super(block);
        this.cause = cause;
        this.ignitingEntity = ignitingEntity;
        this.ignitingBlock = ignitingBlock;
    }

    @Override
    public IgniteCause getCause() {
        return this.cause;
    }

    @Override
    public @Nullable Player getPlayer() {
        if (this.ignitingEntity instanceof final Player ignitingPlayer) {
            return ignitingPlayer;
        }

        return null;
    }

    @Override
    public @Nullable Entity getIgnitingEntity() {
        return this.ignitingEntity;
    }

    @Override
    public @Nullable Block getIgnitingBlock() {
        return this.ignitingBlock;
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
        return BlockIgniteEvent.getHandlerList();
    }
}
