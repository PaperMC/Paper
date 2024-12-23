package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a block is destroyed as a result of being burnt by fire.
 * <p>
 * If a Block Burn event is cancelled, the block will not be destroyed as a
 * result of being burnt by fire.
 *
 * @since 1.0.0
 */
public class BlockBurnEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Block ignitingBlock;

    @Deprecated(since = "1.11.2")
    public BlockBurnEvent(@NotNull final Block block) {
        this(block, null);
    }

    public BlockBurnEvent(@NotNull final Block block, @Nullable final Block ignitingBlock) {
        super(block);
        this.ignitingBlock = ignitingBlock;
    }

    /**
     * Gets the block which ignited this block.
     *
     * @return The Block that ignited and burned this block, or null if no
     * source block exists
     * @since 1.11.2
     */
    @Nullable
    public Block getIgnitingBlock() {
        return ignitingBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * @since 1.1.0
     */
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * @since 1.1.0
     */
    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
