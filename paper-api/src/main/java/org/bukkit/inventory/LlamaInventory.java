package org.bukkit.inventory;

import org.bukkit.entity.Llama;

/**
 * An interface to the inventory of a {@link Llama}.
 */
public interface LlamaInventory extends AbstractHorseInventory {

    /*
     * Gets the item in the llama's decor slot.
     *
     * @return the decor item
     */
    ItemStack getDecor();

    /**
     * Sets the item in the llama's decor slot.
     *
     * @param stack the new item
     */
    void setDecor(ItemStack stack);
}
