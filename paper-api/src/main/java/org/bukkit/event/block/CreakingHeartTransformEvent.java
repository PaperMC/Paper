package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.CreakingHeart.State;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import com.google.common.base.Preconditions;

/**
 * Called when a Creaking Heart block changes its state (e.g., AWAKE, DORMANT, UPROOTED) due to world conditions.
 * <p>
 * Examples:
 * <ul>
 * <li>Creaking Heart becoming AWAKE at night.
 * <li>Creaking Heart becoming DORMANT during the day.
 * <li>Creaking Heart becoming UPROOTED when requirements are not met.
 * </ul>
 * <p>
 */
@NullMarked
public class CreakingHeartTransformEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockState oldState;
    private final BlockState newState;
    private final State oldCreakingHeartState;
    private final State newCreakingHeartState;
    private boolean cancelled;

    /**
     * Constructs a new CreakingHeartTransformEvent.
     *
     * @param block the block being transformed
     * @param newState the new state of the block after transformation
     * @param oldState the previous state of the block before transformation
     */
    @ApiStatus.Internal
    public CreakingHeartTransformEvent(
        Block block,
        BlockState oldState,
        BlockState newState,
        State oldCreakingHeartState,
        State newCreakingHeartState
    ) {
        super(Preconditions.checkNotNull(block, "block cannot be null"));
        this.oldState = Preconditions.checkNotNull(oldState, "oldState cannot be null");
        this.newState = Preconditions.checkNotNull(newState, "newState cannot be null");
        this.oldCreakingHeartState = Preconditions.checkNotNull(oldCreakingHeartState, "oldCreakingHeartState cannot be null");
        this.newCreakingHeartState = Preconditions.checkNotNull(newCreakingHeartState, "newCreakingHeartState cannot be null");
    }

    /**
     * Gets the new state of the block after the transformation.
     *
     * @return the new block state for this event's block
     */
    public BlockState getNewState() {
        return this.newState;
    }

    /**
     * Gets the previous state of the block before the transformation.
     *
     * @return the old block state for this event's block
     */
    public BlockState getOldState() {
        return this.oldState;
    }

    /**
     * Gets the previous logical state of the creaking heart.
     *
     * @return the old logical state
     */
    public State getOldCreakingHeartState() { 
        return this.oldCreakingHeartState; 
    }

    /**
     * Gets the new logical state of the creaking heart.
     *
     * @return the new logical state
     */
    public State getNewCreakingHeartState() { 
        return this.newCreakingHeartState; 
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
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
