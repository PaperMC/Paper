package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot for repair or unenchanting in a grindstone.
 *
 * @since 1.19.3
 */
public class PrepareGrindstoneEvent extends com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent { // Paper

    // Paper - move HandlerList to PrepareInventoryResultEvent

    public PrepareGrindstoneEvent(@NotNull InventoryView inventory, @Nullable ItemStack result) {
        super(inventory, result);
    }

    @NotNull
    @Override
    public GrindstoneInventory getInventory() {
        return (GrindstoneInventory) super.getInventory();
    }

    // Paper - move HandlerList to PrepareInventoryResultEvent
}
