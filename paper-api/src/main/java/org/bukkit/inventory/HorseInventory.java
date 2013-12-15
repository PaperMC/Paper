package org.bukkit.inventory;

/**
 * An interface to the inventory of a Horse.
 */
public interface HorseInventory extends Inventory {

    /**
     * Gets the item in the horse's saddle slot.
     *
     * @return the saddle item
     */
    ItemStack getSaddle();

    /**
     * Gets the item in the horse's armor slot.
     *
     * @return the armor item
     */
    ItemStack getArmor();

    /**
     * Sets the item in the horse's saddle slot.
     *
     * @param stack the new item
     */
    void setSaddle(ItemStack stack);

    /**
     * Sets the item in the horse's armor slot.
     *
     * @param stack the new item
     */
    void setArmor(ItemStack stack);
}
