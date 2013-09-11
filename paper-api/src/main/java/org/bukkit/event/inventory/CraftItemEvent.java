package org.bukkit.event.inventory;

import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

/**
 * Called when the recipe of an Item is completed inside a crafting matrix.
 */
public class CraftItemEvent extends InventoryClickEvent {
    private Recipe recipe;

    @Deprecated
    public CraftItemEvent(Recipe recipe, InventoryView what, SlotType type, int slot, boolean right, boolean shift) {
        this(recipe, what, type, slot, right ? (shift ? ClickType.SHIFT_RIGHT : ClickType.RIGHT) : (shift ? ClickType.SHIFT_LEFT : ClickType.LEFT), InventoryAction.PICKUP_ALL);
    }

    public CraftItemEvent(Recipe recipe, InventoryView what, SlotType type, int slot, ClickType click, InventoryAction action) {
        super(what, type, slot, click, action);
        this.recipe = recipe;
    }

    public CraftItemEvent(Recipe recipe, InventoryView what, SlotType type, int slot, ClickType click, InventoryAction action, int key) {
        super(what, type, slot, click, action, key);
        this.recipe = recipe;
    }

    /**
     * @return A copy of the current recipe on the crafting matrix.
     */
    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public CraftingInventory getInventory() {
        return (CraftingInventory) super.getInventory();
    }
}
