package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when block distance gets changed.
 * <p>
 * If this event is cancelled, the block neighbors will not be updated.
 */
public class LeavesDistanceUpdateEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int minDistance;
    private final int maxDistance;
    private final int oldDistance;
    private int newDistance;
    private boolean cancelled;

    @ApiStatus.Internal
    public LeavesDistanceUpdateEvent(@NotNull final Block block, final int minDistance, final int maxDistance, final int oldDistance, final int newDistance) {
        super(block);
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.oldDistance = oldDistance;
        this.newDistance = newDistance;
    }

    public int getMinDistance() {
        return this.minDistance;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public int getOldDistance() {
        return this.oldDistance;
    }

    public int getNewDistance() {
        return this.newDistance;
    }

    public void setNewDistance(final int newDistance) {
        Preconditions.checkArgument(newDistance >= this.minDistance && newDistance <= this.maxDistance,
            "New distance must be between " + this.minDistance + " and " + this.maxDistance + " but was " + newDistance);
        this.newDistance = newDistance;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
