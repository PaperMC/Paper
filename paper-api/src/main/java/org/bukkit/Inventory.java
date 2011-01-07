package org.bukkit;

import java.util.Collection;

/**
 * Interface to the various inventories
 */
public interface Inventory {
    /**
     * Returns the size of the inventory
     * 
     * @return The inventory size
     */
    public int getSize();

    /**
     * Return the name of the inventory
     * 
     * @return The inventory name
     */
    public String getName();

    /**
     * TODO Set the name of the inventory
     * 
     * @param name The new name of the inventory
    public void setName(String name);
     */

    /** TODO: Appears minecraft has different ideas for slots! 
     * Get the slot at a specific index of an inventory
     * 
     * @param index The index of the slot to get
     * @return The Slot found at the index
    public Slot getSlot(int index);
     */

    /**
     * Get the ItemStack found in the slot at the given index
     * 
     * @param index The index of the Slot's ItemStack to return
     * @return The ItemStack in the slot
     */
    public ItemStack getItem(int index);

    /**
     * Get all ItemStacks from the inventory
     * 
     * @return All the ItemStacks from all slots
     */
    public Collection<ItemStack> getContents();

    /*
     * TODO public boolean contains(int materialId); public boolean
     * contains(Material material); public boolean contains(ItemStack item);
     * 
     * public Collection<Slot> all(int materialId); public Collection<Slot>
     * all(Material material); public Collection<Slot> all(ItemStack item);
     * 
     * public Slot first(int materialId); public Slot first(Material material);
     * public Slot first(ItemStack item);
     * 
     * public int firstEmptyIndex();
     */
}