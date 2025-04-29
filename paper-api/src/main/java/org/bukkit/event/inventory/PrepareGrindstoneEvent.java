package org.bukkit.event.inventory;

import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot for repair or unenchanting in a grindstone.
 */
public class PrepareGrindstoneEvent extends com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent { // Paper

    @ApiStatus.Internal
    public PrepareGrindstoneEvent(@NotNull InventoryView inventory, @Nullable ItemStack result) {
        super(inventory, result);
    }

    @NotNull
    @Override
    public GrindstoneInventory getInventory() {
        return super.getInventory();
    }
}
