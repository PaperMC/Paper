package org.bukkit.event.inventory;

import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Called when the recipe of an Item is completed inside a crafting matrix.
 */
public class CraftItemEvent extends InventoryClickEvent {

    private final Recipe recipe;
    private final List<ItemStack> craftRemainders;

    @ApiStatus.Internal
    public CraftItemEvent(@NotNull Recipe recipe, @NotNull InventoryView view, @NotNull SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action) {
        super(view, type, slot, click, action);
        this.recipe = recipe;
        this.craftRemainders = new ArrayList<>(Collections.nCopies(getInventory().getMatrix().length, null));
    }

    @ApiStatus.Internal
    public CraftItemEvent(@NotNull Recipe recipe, @NotNull InventoryView view, @NotNull SlotType type, int slot, @NotNull ClickType click, @NotNull InventoryAction action, int key) {
        super(view, type, slot, click, action, key);
        this.recipe = recipe;
        this.craftRemainders = new ArrayList<>(Collections.nCopies(getInventory().getMatrix().length, null));
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

    /**
     * @return A list of nullable ItemStacks. The size is the same as the crafting grid's matrix.
     * @apiNote Elements are nullable, meaning only non-null elements actually override the remainder
     */
    public @NotNull List<@Nullable ItemStack> getCraftRemainders() {
        return this.craftRemainders;
    }
}
