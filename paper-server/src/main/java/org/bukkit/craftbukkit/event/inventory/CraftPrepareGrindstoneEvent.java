package org.bukkit.craftbukkit.event.inventory;

import io.papermc.paper.event.inventory.PaperPrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftPrepareGrindstoneEvent extends PaperPrepareGrindstoneEvent implements PrepareGrindstoneEvent {

    public CraftPrepareGrindstoneEvent(final InventoryView inventory, final @Nullable ItemStack result) {
        super(inventory, result);
    }
}
