package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

/**
 * An interface to the inventory of a Horse.
 */
public interface HorseInventory extends AbstractHorseInventory {

    /**
     * Gets the item in the horse's armor slot.
     *
     * @return the armor item
     */
    @Nullable
    ItemStack getArmor();

    /**
     * Sets the item in the horse's armor slot.
     *
     * @param stack the new item
     */
    void setArmor(@Nullable ItemStack stack);
}
