package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a bell is being rung.
 */
public class BellRingEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockFace direction;
    private final Entity entity;

    private boolean cancelled;

    @ApiStatus.Internal
    public BellRingEvent(@NotNull Block block, @NotNull BlockFace direction, @Nullable Entity entity) {
        super(block);
        this.direction = direction;
        this.entity = entity;
    }

    /**
     * Get the direction in which the bell was rung.
     *
     * @return the direction
     */
    @NotNull
    public BlockFace getDirection() {
        return this.direction;
    }

    /**
     * Get the {@link Entity} that rang the bell (if there was one).
     *
     * @return the entity
     */
    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
