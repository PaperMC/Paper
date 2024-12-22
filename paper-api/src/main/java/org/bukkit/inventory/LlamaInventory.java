package org.bukkit.inventory;

import org.bukkit.entity.Llama;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to the inventory of a {@link Llama}.
 *
 * @since 1.11
 */
public interface LlamaInventory extends SaddledHorseInventory {

    /**
     * Gets the item in the llama's decor slot.
     *
     * @return the decor item
     */
    @Nullable
    ItemStack getDecor();

    /**
     * Sets the item in the llama's decor slot.
     *
     * @param stack the new item
     */
    void setDecor(@Nullable ItemStack stack);
}
