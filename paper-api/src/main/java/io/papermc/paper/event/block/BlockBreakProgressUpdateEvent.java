package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when the progress of a block break is updated.
 */
@NullMarked
public class BlockBreakProgressUpdateEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final float progress;
    private final Entity entity;

    @ApiStatus.Internal
    public BlockBreakProgressUpdateEvent(final Block block, final float progress, final Entity entity) {
        super(block);
        this.progress = progress;
        this.entity = entity;
    }

    /**
     * The progress of the block break
     * <p>
     * The progress ranges from 0.0 - 1.0, where 0 is no damage and
     * 1.0 is the most damaged
     *
     * @return The progress of the block break
     */
    public float getProgress() {
        return this.progress;
    }

    /**
     * The entity breaking the block.
     *
     * @return The entity breaking the block
     */
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
