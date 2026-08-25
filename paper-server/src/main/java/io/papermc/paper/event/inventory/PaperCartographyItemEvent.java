package io.papermc.paper.event.inventory;

import io.papermc.paper.event.player.CartographyItemEvent;
import org.bukkit.craftbukkit.event.inventory.CraftInventoryClickEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.InventoryView;

public class PaperCartographyItemEvent extends CraftInventoryClickEvent implements CartographyItemEvent {

    public PaperCartographyItemEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action) {
        super(view, type, slot, click, action);
    }

    public PaperCartographyItemEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action, final int key) {
        super(view, type, slot, click, action, key);
    }

    @Override
    public CartographyInventory getInventory() {
        return (CartographyInventory) super.getInventory();
    }
}
