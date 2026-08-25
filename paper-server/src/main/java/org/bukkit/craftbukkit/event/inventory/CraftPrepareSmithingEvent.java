package org.bukkit.craftbukkit.event.inventory;

import io.papermc.paper.event.inventory.PaperPrepareResultEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;
import org.jspecify.annotations.Nullable;

public class CraftPrepareSmithingEvent extends PaperPrepareResultEvent implements PrepareSmithingEvent {

    public CraftPrepareSmithingEvent(final InventoryView inventory, final @Nullable ItemStack result) {
        super(inventory, result);
    }

    @Override
    public SmithingInventory getInventory() {
        return (SmithingInventory) super.getInventory();
    }
}
