package org.bukkit.block;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a campfire.
 */
public interface Campfire extends TileState {

    /**
     * @return The size of the inventory
     * @see Inventory#getSize()
     */
    int getSize();

    /**
     * @param index The index of the Slot's ItemStack to return
     * @return The ItemStack in the slot
     * @see Inventory#getItem(int)
     */
    @Nullable
    ItemStack getItem(int index);

    /**
     * @param index The index where to put the ItemStack
     * @param item The ItemStack to set
     * @see Inventory#setItem(int, org.bukkit.inventory.ItemStack)
     */
    void setItem(int index, @Nullable ItemStack item);

    /**
     * Get cook time.
     *
     * This is the amount of time the item has been cooking for.
     *
     * @param index item slot index
     * @return Cook time
     */
    int getCookTime(int index);

    /**
     * Set cook time.
     *
     * This is the amount of time the item has been cooking for.
     *
     * @param index item slot index
     * @param cookTime Cook time
     */
    void setCookTime(int index, int cookTime);

    /**
     * Get cook time total.
     *
     * This is the amount of time the item is required to cook for.
     *
     * @param index item slot index
     * @return Cook time total
     */
    int getCookTimeTotal(int index);

    /**
     * Set cook time.
     *
     * This is the amount of time the item is required to cook for.
     *
     * @param index item slot index
     * @param cookTimeTotal Cook time total
     */
    void setCookTimeTotal(int index, int cookTimeTotal);
}
