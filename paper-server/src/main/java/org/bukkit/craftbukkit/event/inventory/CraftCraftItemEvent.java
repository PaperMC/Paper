package org.bukkit.craftbukkit.event.inventory;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

public class CraftCraftItemEvent extends CraftInventoryClickEvent implements CraftItemEvent {

    private final Recipe recipe;

    public CraftCraftItemEvent(final Recipe recipe, final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action) {
        super(view, type, slot, click, action);
        this.recipe = recipe;
    }

    public CraftCraftItemEvent(final Recipe recipe, final InventoryView view, final InventoryType.SlotType type, final int slot, final ClickType click, final InventoryAction action, final int key) {
        super(view, type, slot, click, action, key);
        this.recipe = recipe;
    }

    @Override
    public CraftingInventory getInventory() {
        return (CraftingInventory) super.getInventory();
    }

    @Override
    public Recipe getRecipe() {
        return this.recipe;
    }
}
