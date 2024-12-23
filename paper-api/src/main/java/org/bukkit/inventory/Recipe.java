package org.bukkit.inventory;

import org.jetbrains.annotations.NotNull;

/**
 * Represents some type of crafting recipe.
 *
 * @since 1.0.0
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
