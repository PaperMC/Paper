package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.inventory.CraftingRecipe;
import org.bukkit.inventory.ItemStack;

public class CraftCrafterCraftEvent extends CraftBlockEvent implements CrafterCraftEvent {

    private final CraftingRecipe recipe;
    private ItemStack result;

    private boolean cancelled;

    public CraftCrafterCraftEvent(final Block crafter, final CraftingRecipe recipe, final ItemStack result) {
        super(crafter);
        this.result = result;
        this.recipe = recipe;
    }

    @Override
    public ItemStack getResult() {
        return this.result.clone();
    }

    @Override
    public void setResult(final ItemStack result) {
        this.result = result.clone();
    }

    @Override
    public CraftingRecipe getRecipe() {
        return this.recipe;
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
        return CrafterCraftEvent.getHandlerList();
    }
}
