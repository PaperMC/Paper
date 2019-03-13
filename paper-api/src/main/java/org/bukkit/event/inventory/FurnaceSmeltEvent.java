package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully smelted in a furnace.
 */
public class FurnaceSmeltEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack source;
    private ItemStack result;
    private boolean cancelled;

    public FurnaceSmeltEvent(@NotNull final Block furnace, @NotNull final ItemStack source, @NotNull final ItemStack result) {
        super(furnace);
        this.source = source;
        this.result = result;
        this.cancelled = false;
    }

    /**
     * Gets the smelted ItemStack for this event
     *
     * @return smelting source ItemStack
     */
    @NotNull
    public ItemStack getSource() {
        return source;
    }

    /**
     * Gets the resultant ItemStack for this event
     *
     * @return smelting result ItemStack
     */
    @NotNull
    public ItemStack getResult() {
        return result;
    }

    /**
     * Sets the resultant ItemStack for this event
     *
     * @param result new result ItemStack
     */
    public void setResult(@NotNull ItemStack result) {
        this.result = result;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
