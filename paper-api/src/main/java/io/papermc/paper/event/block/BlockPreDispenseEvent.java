package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * @since 1.16.5
 */
@NullMarked
public class BlockPreDispenseEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack itemStack;
    private final int slot;

    private boolean cancelled;

    @ApiStatus.Internal
    public BlockPreDispenseEvent(final Block block, final ItemStack itemStack, final int slot) {
        super(block);
        this.itemStack = itemStack;
        this.slot = slot;
    }

    /**
     * Gets the {@link ItemStack} to be dispensed.
     *
     * @return The item to be dispensed
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Gets the inventory slot of the dispenser to dispense from.
     *
     * @return The inventory slot
     */
    public int getSlot() {
        return this.slot;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
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
