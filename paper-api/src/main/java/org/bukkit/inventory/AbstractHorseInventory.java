package org.bukkit.inventory;

import org.bukkit.entity.AbstractHorse;

/**
 * An interface to the inventory of an {@link AbstractHorse}.
 */
public interface AbstractHorseInventory extends Inventory {

    /**
     * Gets the item in the horse's saddle slot.
     *
     * @return the saddle item
     */
    ItemStack getSaddle();

    /**
     * Sets the item in the horse's saddle slot.
     *
     * @param stack the new item
     */
    void setSaddle(ItemStack stack);
}
