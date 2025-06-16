package org.bukkit.event.inventory;

import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Called when the recipe of an Item is completed inside a crafting matrix.
 */
public class CraftItemEvent extends InventoryClickEvent {

    private final Recipe recipe;
    private final List<ItemStack> craftRemainders; // Paper - Add Craft Remainders

    @ApiStatus.Internal
    public CraftItemEvent(@NotNull Recipe recipe, @NotNull InventoryView view, @NotNull SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action) {
        super(view, type, slot, click, action);
        this.recipe = recipe;
        this.craftRemainders = new ArrayList<>(Collections.nCopies(getInventory().getMatrix().length, null)); // Paper - Initialize Craft Remainders
    }

    @ApiStatus.Internal
    public CraftItemEvent(@NotNull Recipe recipe, @NotNull InventoryView view, @NotNull SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action, int key) {
        super(view, type, slot, click, action, key);
        this.recipe = recipe;
        this.craftRemainders = new ArrayList<>(Collections.nCopies(getInventory().getMatrix().length, null)); // Paper - Initialize Craft Remainders
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

    // Paper start - Allows for replacing remainders
    public List<ItemStack> getCraftRemainders() {
        return this.craftRemainders;
    }
    // Paper end - Allows for replacing remainders
}
