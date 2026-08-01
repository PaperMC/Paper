package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an ItemStack is successfully cooked in a block.
 */
public class BlockCookEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack source;
    private ItemStack result;
    private final CookingRecipe<?> recipe;

    private boolean cancelled;

    @Deprecated(forRemoval = true)
    @ApiStatus.Internal
    public BlockCookEvent(@NotNull final Block block, @NotNull final ItemStack source, @NotNull final ItemStack result) {
        this(block, source, result, null);
    }

    @ApiStatus.Internal
    public BlockCookEvent(@NotNull final Block block, @NotNull final ItemStack source, @NotNull final ItemStack result, @Nullable CookingRecipe<?> recipe) {
        super(block);
        this.source = source;
        this.result = result;
        this.recipe = recipe;
    }

    /**
     * Gets the smelted ItemStack for this event
     *
     * @return smelting source ItemStack
     */
    @NotNull
    public ItemStack getSource() {
        return this.source;
    }

    /**
     * Gets the resultant ItemStack for this event
     *
     * @return smelting result ItemStack
     */
    @NotNull
    public ItemStack getResult() {
        return this.result;
    }

    /**
     * Sets the resultant ItemStack for this event
     *
     * @param result new result ItemStack
     */
    public void setResult(@NotNull ItemStack result) {
        this.result = result;
    }

    /**
     * Gets the cooking recipe associated with this event.
     *
     * @return the recipe
     */
    @Nullable
    public org.bukkit.inventory.CookingRecipe<?> getRecipe() {
        return this.recipe;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
