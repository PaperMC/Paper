package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an ItemStack is successfully cooked in a block.
 */
public class BlockCookEvent extends BlockEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack source;
    private ItemStack result;
    private boolean cancelled;
    private final org.bukkit.inventory.CookingRecipe<?> recipe; // Paper

    @Deprecated // Paper
    public BlockCookEvent(@NotNull final Block block, @NotNull final ItemStack source, @NotNull final ItemStack result) {
        // Paper start
        this(block, source, result, null);
    }

    public BlockCookEvent(@NotNull final Block block, @NotNull final ItemStack source, @NotNull final ItemStack result, @org.jetbrains.annotations.Nullable org.bukkit.inventory.CookingRecipe<?> recipe) {
        // Paper end
        super(block);
        this.source = source;
        this.result = result;
        this.cancelled = false;
        this.recipe = recipe; // Paper
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    // Paper start
    /**
     * Gets the cooking recipe associated with this event.
     *
     * @return the recipe
     */
    @org.jetbrains.annotations.Nullable
    public org.bukkit.inventory.CookingRecipe<?> getRecipe() {
        return recipe;
    }
    // Paper end

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
