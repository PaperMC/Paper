package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a Crafter is about to craft an item.
 */
public class CrafterCraftEvent extends BlockEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final CraftingRecipe recipe;
    private ItemStack result;

    private boolean cancelled;

    @ApiStatus.Internal
    public CrafterCraftEvent(@NotNull Block crafter, @NotNull CraftingRecipe recipe, @NotNull ItemStack result) {
        super(crafter);
        this.result = result;
        this.recipe = recipe;
    }

    /**
     * Gets the result for the craft.
     *
     * @return the result for the craft
     */
    @NotNull
    public ItemStack getResult() {
        return this.result.clone();
    }

    /**
     * Sets the result of the craft.
     *
     * @param result the result of the craft
     */
    public void setResult(@NotNull ItemStack result) {
        this.result = result.clone();
    }

    /**
     * Gets the recipe that was used to craft this item.
     *
     * @return the recipe that was used to craft this item
     */
    @NotNull
    public CraftingRecipe getRecipe() {
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
