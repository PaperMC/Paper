package org.bukkit.inventory;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Container class containing the results of a Crafting event.
 * <br>
 * This class makes no guarantees about the nature or mutability of the returned
 * values.
 */
public interface ItemCraftResult {

    /**
     * The resulting {@link ItemStack} that was crafted.
     *
     * @return {@link ItemStack} that was crafted.
     */
    @NotNull
    public ItemStack getResult();

    /**
     * Gets the resulting matrix from the crafting operation.
     *
     * @return resulting matrix
     */
    @NotNull
    public ItemStack[] getResultingMatrix();

    /**
     * Gets the overflowed items for items that don't fit back into the crafting
     * matrix.
     *
     * @return overflow items
     */
    @NotNull
    public List<ItemStack> getOverflowItems();
}
