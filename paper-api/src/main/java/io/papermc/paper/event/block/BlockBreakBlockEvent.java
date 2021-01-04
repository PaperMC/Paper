package io.papermc.paper.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a block forces another block to break and drop items.
 * <p>
 * Currently called for piston's and liquid flows.
 */
@NullMarked
public class BlockBreakBlockEvent extends BlockExpEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Block source;
    private final List<ItemStack> drops;

    @ApiStatus.Internal
    public BlockBreakBlockEvent(final Block block, final Block source, final List<ItemStack> drops) {
        super(block, 0);
        this.source = source;
        this.drops = drops;
    }

    /**
     * Gets a mutable list of drops for this event
     *
     * @return the drops
     */
    public List<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Gets the block that cause this (e.g. a piston, or adjacent liquid)
     *
     * @return the source
     */
    public Block getSource() {
        return this.source;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
