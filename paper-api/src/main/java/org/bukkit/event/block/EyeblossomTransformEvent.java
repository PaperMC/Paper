package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an Eyeblossom opens or closes (transforms its state) due to world conditions.
 * <p>
 * Examples:
 * <ul>
 * <li>Eyeblossom opening at night.
 * <li>Eyeblossom closing during the day.
 * </ul>
 * <p>
 * If this event is cancelled, the block will not transform.
 */
public class EyeblossomTransformEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockState newState;
    private boolean cancelled;

    /**
     * Constructs a new EyeblossomTransformEvent.
     *
     * @param block the block being transformed
     * @param newState the new state of the block after transformation
     */
    public EyeblossomTransformEvent(@NotNull final Block block, @NotNull final BlockState newState) {
        super(Preconditions.checkNotNull(block, "block"));
        this.newState = Preconditions.checkNotNull(newState, "newState");
    }

    /**
     * Gets the new state of the block after the transformation.
     *
     * @return the new block state for this event's block
     */
    @NotNull
    public BlockState getNewState() {
        return this.newState;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
