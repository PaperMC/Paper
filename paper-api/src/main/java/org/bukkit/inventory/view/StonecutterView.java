package org.bukkit.inventory.view;

import java.util.List;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.StonecutterInventory;
import org.bukkit.inventory.StonecuttingRecipe;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * stonecutter view data.
 */
public interface StonecutterView extends InventoryView {

    @NotNull
    @Override
    StonecutterInventory getTopInventory();

    /**
     * Gets the current index of the selected recipe.
     *
     * @return The index of the selected recipe in the stonecutter or -1 if null
     */
    int getSelectedRecipeIndex();

    /**
     * Gets a copy of all recipes currently available to the player.
     *
     * @return A copy of the {@link StonecuttingRecipe}'s currently available
     * for the player
     */
    @NotNull
    List<StonecuttingRecipe> getRecipes();

    /**
     * Gets the amount of recipes currently available.
     *
     * @return The amount of recipes currently available for the player
     */
    int getRecipeAmount();
}
