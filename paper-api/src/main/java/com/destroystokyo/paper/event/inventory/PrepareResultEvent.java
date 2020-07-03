package com.destroystokyo.paper.event.inventory;

import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when an item is put in an inventory containing a result slot
 */
@NullMarked
public class PrepareResultEvent extends PrepareInventoryResultEvent {

    // HandlerList on PrepareInventoryResultEvent to ensure api compat

    @ApiStatus.Internal
    public PrepareResultEvent(final InventoryView inventory, final @Nullable ItemStack result) {
        super(inventory, result);
    }

    /**
     * Get result item, may be {@code null}.
     *
     * @return result item
     */
    @Override
    public @Nullable ItemStack getResult() {
        return super.getResult();
    }

    /**
     * Set result item, may be {@code null}.
     *
     * @param result result item
     */
    @Override
    public void setResult(final @Nullable ItemStack result) {
        super.setResult(result);
    }
}
