package org.bukkit.event.block;

import io.papermc.paper.event.ReceiveGameEvent;
import org.bukkit.GameEvent;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block receives a game event and hence might take some action.
 * <p>
 * Will be called cancelled if the block's default behavior is to ignore the
 * event.
 */
@NullMarked
public class BlockReceiveGameEvent extends BlockEvent implements ReceiveGameEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final GameEvent event;
    private final @Nullable Entity triggerEntity;
    private final @Nullable BlockData triggerBlockData;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockReceiveGameEvent(final GameEvent event, final Block block, final @Nullable Entity triggerEntity, final @Nullable BlockData triggerBlockData) {
        super(block);
        this.event = event;
        this.triggerEntity = triggerEntity;
        this.triggerBlockData = triggerBlockData;
    }

    @Override
    public GameEvent getEvent() {
        return this.event;
    }

    /**
     * Get the entity which triggered this event, if present.
     *
     * @return triggering entity or {@code null}
     * @deprecated use {@link #getTriggerEntity()}
     */
    @Deprecated(since = "1.21.11")
    public @Nullable Entity getEntity() {
        return this.getTriggerEntity();
    }

    @Override
    public @Nullable Entity getTriggerEntity() {
        return this.triggerEntity;
    }

    @Override
    public @Nullable BlockData getTriggerBlockData() {
        return this.triggerBlockData == null ? null : this.triggerBlockData.clone();
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
