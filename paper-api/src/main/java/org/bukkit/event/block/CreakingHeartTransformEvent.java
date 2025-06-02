package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.bukkit.block.CreakingHeartState;

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
 * If this event is cancelled, the block will not change states.
 */
public class CreakingHeartTransformEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final BlockState oldState;
    private final BlockState newState;
    private final CreakingHeartState oldLogicalState;
    private final CreakingHeartState newLogicalState;
    private boolean cancelled;

    /**
     * Constructs a new CreakingHeartTransformEvent.
     *
     * @param block the block being transformed
     * @param newState the new state of the block after transformation
     * @param oldState the previous state of the block before transformation
     */
    public CreakingHeartTransformEvent(
        @NotNull Block block,
        @NotNull BlockState oldState,
        @NotNull BlockState newState,
        @NotNull CreakingHeartState oldLogicalState,
        @NotNull CreakingHeartState newLogicalState
    ) {
        super(Preconditions.checkNotNull(block, "block cannot be null"));
        this.oldState = Preconditions.checkNotNull(oldState, "oldState cannot be null");
        this.newState = Preconditions.checkNotNull(newState, "newState cannot be null");
        this.oldLogicalState = Preconditions.checkNotNull(oldLogicalState, "oldLogicalState cannot be null");
        this.newLogicalState = Preconditions.checkNotNull(newLogicalState, "newLogicalState cannot be null");
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

    /**
     * Gets the previous state of the block before the transformation.
     *
     * @return the old block state for this event's block
     */
    @NotNull
    public BlockState getOldState() {
        return this.oldState;
    }

    /**
     * Gets the previous logical state of the Creaking Heart.
     *
     * @return the old logical state
     */
    @NotNull
    public CreakingHeartState getOldLogicalState() { 
        return this.oldLogicalState; 
    }

    /**
     * Gets the new logical state of the Creaking Heart.
     *
     * @return the new logical state
     */
    @NotNull
    public CreakingHeartState getNewLogicalState() { 
        return this.newLogicalState; 
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
