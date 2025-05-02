package org.bukkit.event.inventory;

import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Called when the recipe of an Item is completed inside a crafting matrix.
 */
public class CraftItemEvent extends InventoryClickEvent {

    private final Recipe recipe;

    @ApiStatus.Internal
    public CraftItemEvent(@NotNull Recipe recipe, @NotNull InventoryView view, @NotNull SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action) {
        super(view, type, slot, click, action);
        this.recipe = recipe;
    }

    @ApiStatus.Internal
    public CraftItemEvent(@NotNull Recipe recipe, @NotNull InventoryView view, @NotNull SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action, int key) {
        super(view, type, slot, click, action, key);
        this.recipe = recipe;
    }

    @NotNull
    @Override
    public CraftingInventory getInventory() {
        return (CraftingInventory) super.getInventory();
    }

    /**
     * @return A copy of the current recipe on the crafting matrix.
     */
    @NotNull
    public Recipe getRecipe() {
        return this.recipe;
    }
}
