package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.TNTPrimeEvent;
import org.jspecify.annotations.Nullable;

public class CraftTNTPrimeEvent extends CraftBlockEvent implements TNTPrimeEvent {

    private final PrimeCause igniteCause;
    private final @Nullable Entity primingEntity;
    private final @Nullable Block primingBlock;

    private boolean cancelled;

    public CraftTNTPrimeEvent(final Block block, final PrimeCause igniteCause, final @Nullable Entity primingEntity, final @Nullable Block primingBlock) {
        super(block);
        this.igniteCause = igniteCause;
        this.primingEntity = primingEntity;
        this.primingBlock = primingBlock;
    }

    @Override
    public PrimeCause getCause() {
        return this.igniteCause;
    }

    @Override
    public @Nullable Entity getPrimingEntity() {
        return this.primingEntity;
    }

    @Override
    public @Nullable Block getPrimingBlock() {
        return this.primingBlock;
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
        return TNTPrimeEvent.getHandlerList();
    }
}
