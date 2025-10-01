package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.CopperGolem;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CopperGolemValidateTargetEvent extends EntityEvent implements Cancellable {
    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private boolean cancelled;

    @ApiStatus.Internal
    public CopperGolemValidateTargetEvent(
        final CopperGolem copperGolem,
        final Block block
    ) {
        super(copperGolem);
        this.block = block;
    }

    @Override
    public CopperGolem getEntity() {
        return (CopperGolem) super.getEntity();
    }

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

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
