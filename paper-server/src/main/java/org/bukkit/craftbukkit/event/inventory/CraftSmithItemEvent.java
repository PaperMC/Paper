package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.SmithingInventory;

public class CraftSmithItemEvent extends CraftInventoryClickEvent implements SmithItemEvent {

    public CraftSmithItemEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action) {
        super(view, type, slot, click, action);
    }

    public CraftSmithItemEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action, final int key) {
        super(view, type, slot, click, action, key);
    }

    @Override
    public SmithingInventory getInventory() {
        return (SmithingInventory) super.getInventory();
    }
}
