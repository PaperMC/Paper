package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;

/**
 * Interface to the inventory of a Player, including the four armor slots.
 */
public interface PlayerInventory extends Inventory {

    /**
     * Get all ItemStacks from the armor slots
     *
     * @return All the ItemStacks from the armor slots
     */
    public ItemStack[] getArmorContents();

    /**
     * Return the ItemStack from the helmet slot
     *
     * @return The ItemStack in the helmet slot
     */
    public ItemStack getHelmet();

    /**
     * Return the ItemStack from the chestplate slot
     *
     * @return The ItemStack in the chestplate slot
     */
    public ItemStack getChestplate();

    /**
     * Return the ItemStack from the leg slot
     *
     * @return The ItemStack in the leg slot
     */
    public ItemStack getLeggings();

    /**
     * Return the ItemStack from the boots slot
     *
     * @return The ItemStack in the boots slot
     */
    public ItemStack getBoots();

    /**
     * Put the given ItemStacks into the armor slots
     *
     * @param items The ItemStacks to use as armour
     */
    public void setArmorContents(ItemStack[] items);

    /**
     * Put the given ItemStack into the helmet slot. This does not check if
     * the ItemStack is a helmet
     *
     * @param helmet The ItemStack to use as helmet
     */
    public void setHelmet(ItemStack helmet);

    /**
     * Put the given ItemStack into the chestplate slot. This does not check
     * if the ItemStack is a chestplate
     *
     * @param chestplate The ItemStack to use as chestplate
     */
    public void setChestplate(ItemStack chestplate);

    /**
     * Put the given ItemStack into the leg slot. This does not check if the
     * ItemStack is a pair of leggings
     *
     * @param leggings The ItemStack to use as leggings
     */
    public void setLeggings(ItemStack leggings);

    /**
     * Put the given ItemStack into the boots slot. This does not check if the
     * ItemStack is a boots
     *
     * @param boots The ItemStack to use as boots
     */
    public void setBoots(ItemStack boots);

    /**
     * Returns the ItemStack currently hold
     *
     * @return The currently held ItemStack
     */
    public ItemStack getItemInHand();

    /**
     * Sets the item in hand
     *
     * @param stack Stack to set
     */
    public void setItemInHand(ItemStack stack);

    /**
     * Get the slot number of the currently held item
     *
     * @return Held item slot number
     */
    public int getHeldItemSlot();

    /**
     * Set the slot number of the currently held item.
     * <p>
     * This validates whether the slot is between 0 and 8 inclusive.
     *
     * @param slot The new slot number
     * @throws IllegalArgumentException Thrown if slot is not between 0 and 8
     *     inclusive
     */
    public void setHeldItemSlot(int slot);

    /**
     * Clears all matching items from the inventory. Setting either value to
     * -1 will skip it's check, while setting both to -1 will clear all items
     * in your inventory unconditionally.
     *
     * @param id the id of the item you want to clear from the inventory
     * @param data the data of the item you want to clear from the inventory
     * @return The number of items cleared
     * @deprecated Magic value
     */
    @Deprecated
    public int clear(int id, int data);

    public HumanEntity getHolder();
}
