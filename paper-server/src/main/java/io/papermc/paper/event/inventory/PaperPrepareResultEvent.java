package io.papermc.paper.event.inventory;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.craftbukkit.event.inventory.CraftPrepareInventoryResultEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class PaperPrepareResultEvent extends CraftPrepareInventoryResultEvent implements PrepareResultEvent {
    // HandlerList on PrepareInventoryResultEvent to ensure api compat

    public PaperPrepareResultEvent(final InventoryView inventory, final @Nullable ItemStack result) {
        super(inventory, result);
    }
}
