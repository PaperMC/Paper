package org.bukkit.inventory;

import org.jetbrains.annotations.NotNull;

/**
 * Represents some type of crafting recipe.
 */
public interface Recipe {

    /**
     * Get the result of this recipe.
     *
     * @return The result stack
     */
    @NotNull
    ItemStack getResult();
}
