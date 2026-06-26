package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

/**
    Called when a block is about to drop items

 <p>
    This event will be called once for every block broken, even if multiple blocks are broken simultaneously.
    For example, this event will be called twice when breaking a block with a torch on top.
 </p>
 <p>
    This event will also be called when a block is dropping items as a result of being consumed, for example a cake dropping its candle after being eaten.
 </p>
 <p>
    If you do not need the drops of each individual block, use {@link org.bukkit.event.block.BlockBreakEvent}.
 </p>
 */
public class BlockDropResourcesEvent extends BlockEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancelled;
    private final List<ItemStack> drops;
    private final Entity breaker;
    private final ItemStack tool;
    private final BlockState state;

    @ApiStatus.Internal
    public BlockDropResourcesEvent(final @NotNull Block block, final @NotNull BlockState state, final @NotNull List<ItemStack> drops, final @Nullable Entity breaker, final @Nullable ItemStack tool) {
        super(block);
        this.cancelled = false;
        this.drops = drops;
        this.breaker = breaker;
        this.tool = tool;
        this.state = state;
    }

    /**
     * Get the entity that caused the block to drop resources
     * @return The responsible entity, or null if no entity was involved
     */
    public @Nullable Entity getEntity() {
        return breaker;
    }

    /**
     * Get the tool used to break the block
     * @return The tool used, or null
     */
    public @Nullable ItemStack getTool() {
        return tool;
    }

    /**
     * Get the list of items to be dropped
     *
     * @return A mutable list of items to be dropped
     */
    public @NotNull List<ItemStack> getItems() {
        return drops;
    }

    /**
     * Get the block state of the block that is about to drop resources
     * @return The block state
     */
    public @NotNull BlockState getBlockState() {
        return state;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
    public @NotNull static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
