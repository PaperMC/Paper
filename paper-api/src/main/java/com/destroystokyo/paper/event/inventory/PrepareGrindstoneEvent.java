package com.destroystokyo.paper.event.inventory;

import org.bukkit.Warning;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when an item is put in a slot for grinding in a Grindstone
 *
 * @deprecated use {@link org.bukkit.event.inventory.PrepareGrindstoneEvent}
 */
@Deprecated(since = "1.16.1")
@Warning
public class PrepareGrindstoneEvent extends PrepareResultEvent {

    @ApiStatus.Internal
    public PrepareGrindstoneEvent(@NotNull InventoryView inventory, @Nullable ItemStack result) {
        super(inventory, result);
    }

    @NotNull
    @Override
    public GrindstoneInventory getInventory() {
        return (GrindstoneInventory) super.getInventory();
    }

}
