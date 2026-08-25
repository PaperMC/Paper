package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.PrepareInventoryResultEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

@Deprecated
public class CraftPrepareInventoryResultEvent extends CraftInventoryEvent implements PrepareInventoryResultEvent {

    private ItemStack result;

    public CraftPrepareInventoryResultEvent(final InventoryView inventory, final @Nullable ItemStack result) {
        super(inventory);
        this.result = result;
    }

    @Override
    public @Nullable ItemStack getResult() {
        return this.result;
    }

    @Override
    public void setResult(final @Nullable ItemStack result) {
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return PrepareInventoryResultEvent.getHandlerList();
    }
}
