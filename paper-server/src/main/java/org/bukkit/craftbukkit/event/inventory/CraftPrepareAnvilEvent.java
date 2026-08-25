package org.bukkit.craftbukkit.event.inventory;

import io.papermc.paper.event.inventory.PaperPrepareResultEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.view.AnvilView;
import org.jspecify.annotations.Nullable;

public class CraftPrepareAnvilEvent extends PaperPrepareResultEvent implements PrepareAnvilEvent {

    public CraftPrepareAnvilEvent(final AnvilView inventory, final @Nullable ItemStack result) {
        super(inventory, result);
    }

    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @Override
    public AnvilView getView() {
        return (AnvilView) super.getView();
    }
}
