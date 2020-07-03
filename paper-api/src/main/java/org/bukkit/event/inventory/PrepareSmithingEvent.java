package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot for upgrade by a Smithing Table.
 */
public class PrepareSmithingEvent extends com.destroystokyo.paper.event.inventory.PrepareResultEvent {

    // Paper - move HandlerList ot PrepareInventoryResultEvent

    public PrepareSmithingEvent(@NotNull InventoryView inventory, @Nullable ItemStack result) {
        super(inventory, result);
    }

    @NotNull
    @Override
    public SmithingInventory getInventory() {
        return (SmithingInventory) super.getInventory();
    }

    // Paper - move HandlerList to PrepareInventoryResultEvent
}
