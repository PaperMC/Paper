package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftBlockCookEvent extends CraftBlockEvent implements BlockCookEvent {

    private final ItemStack source;
    private ItemStack result;
    private final @Nullable CookingRecipe<?> recipe;

    private boolean cancelled;

    public CraftBlockCookEvent(final Block block, final ItemStack source, final ItemStack result, final @Nullable CookingRecipe<?> recipe) {
        super(block);
        this.source = source;
        this.result = result;
        this.recipe = recipe;
    }

    @Override
    public ItemStack getSource() {
        return this.source;
    }

    @Override
    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public void setResult(final ItemStack result) {
        this.result = result;
    }

    @Override
    public @Nullable CookingRecipe<?> getRecipe() {
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
        return BlockCookEvent.getHandlerList();
    }
}
