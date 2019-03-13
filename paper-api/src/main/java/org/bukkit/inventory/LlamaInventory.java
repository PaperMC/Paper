package org.bukkit.inventory;

import org.bukkit.entity.Llama;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to the inventory of a {@link Llama}.
 */
public interface LlamaInventory extends AbstractHorseInventory {

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
