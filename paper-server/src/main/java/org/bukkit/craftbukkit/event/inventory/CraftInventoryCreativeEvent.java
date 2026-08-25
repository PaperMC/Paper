package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryCreativeEvent extends CraftInventoryClickEvent implements InventoryCreativeEvent {

    private ItemStack item;

    public CraftInventoryCreativeEvent(final InventoryView view, final InventoryType.SlotType type, final int slot, final ItemStack newItem) {
        super(view, type, slot, ClickType.CREATIVE, InventoryAction.PLACE_ALL);
        this.item = newItem;
    }

    @Override
    public ItemStack getCursor() {
        return this.item;
    }

    @Override
    public void setCursor(final ItemStack item) {
        this.item = item;
    }
}
