package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.CopperGolem;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CopperGolemValidateTargetEvent extends EntityEvent {
    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block block;
    private boolean allowed = true;

    @ApiStatus.Internal
    public CopperGolemValidateTargetEvent(
        final CopperGolem copperGolem,
        final Block block
    ) {
        super(copperGolem);
        this.block = block;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public CopperGolem getEntity() {
        return (CopperGolem) super.getEntity();
    }

    public Block getBlock() {
        return this.block;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
