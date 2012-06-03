package org.bukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;

/**
 * Interface to the various inventories
 */
public interface Inventory extends Iterable<ItemStack> {

    /**
     * Returns the size of the inventory
     *
     * @return The inventory size
     */
    public int getSize();

    /**
     * @return The maximum stack size for items in this inventory.
     */
    public int getMaxStackSize();

    /**
     * This method allows you to change the maximum stack size for an inventory.
     * <p /><b>Caveats:</b>
     * <ul>
     * <li>Not all inventories respect this value.
     * <li>Stacks larger than 127 may be clipped when the world is saved.
     * <li>This value is not guaranteed to be preserved; be sure to set it before every time
     * you want to set a slot over the max stack size.
     * <li>Stacks larger than the default max size for this type of inventory may not display
     * correctly in the client.
     * </ul>
     * @param size The new maximum stack size for items in this inventory.
     */
    public void setMaxStackSize(int size);

    /**
     * Return the name of the inventory
     *
     * @return The inventory name
     */
    public String getName();

    /**
     * Get the ItemStack found in the slot at the given index
     *
     * @param index The index of the Slot's ItemStack to return
     * @return The ItemStack in the slot
     */
    public ItemStack getItem(int index);

    /**
     * Stores the ItemStack at the given index
     *
     * @param index The index where to put the ItemStack
     * @param item The ItemStack to set
     */
    public void setItem(int index, ItemStack item);

    /**
     * Stores the given ItemStacks in the inventory.
     * This will try to fill existing stacks and empty slots as good as it can.
     * It will return a HashMap of what it couldn't fit.
     *
     * @param items The ItemStacks to add
     * @return The items that didn't fit.
     */
    public HashMap<Integer, ItemStack> addItem(ItemStack... items);

    /**
     * Removes the given ItemStacks from the inventory.
     * <p />
     * It will try to remove 'as much as possible' from the types and amounts you
     * give as arguments. It will return a HashMap of what it couldn't remove.
     *
     * @param items The ItemStacks to remove
     * @return The items that couldn't be removed.
     */
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items);

    /**
     * Get all ItemStacks from the inventory
     *
     * @return All the ItemStacks from all slots
     */
    public ItemStack[] getContents();

    /**
     * Set the inventory's contents
     *
     * @param items A complete replacement for the contents; the length must be less than or equal to {@link #getSize()}.
     * @throws IllegalArgumentException If the array has more items than the inventory.
     */
    public void setContents(ItemStack[] items);

    /**
     * Check if the inventory contains any ItemStacks with the given materialId
     *
     * @param materialId The materialId to check for
     * @return If any ItemStacks were found
     */
    public boolean contains(int materialId);

    /**
     * Check if the inventory contains any ItemStacks with the given material
     *
     * @param material The material to check for
     * @return If any ItemStacks were found
     */
    public boolean contains(Material material);

    /**
     * Check if the inventory contains any ItemStacks matching the given ItemStack
     * This will only match if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return If any matching ItemStacks were found
     */
    public boolean contains(ItemStack item);

    /**
     * Check if the inventory contains any ItemStacks with the given materialId and at least the minimum amount specified
     *
     * @param materialId The materialId to check for
     * @param amount The minimum amount to look for
     * @return If any ItemStacks were found
     */
    public boolean contains(int materialId, int amount);

    /**
     * Check if the inventory contains any ItemStacks with the given material and at least the minimum amount specified
     *
     * @param material The material to check for
     * @param amount The minimum amount
     * @return If any ItemStacks were found
     */
    public boolean contains(Material material, int amount);

    /**
     * Check if the inventory contains any ItemStacks matching the given ItemStack and at least the minimum amount specified
     * This will only match if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @param amount The minimum amount
     * @return If any matching ItemStacks were found
     */
    public boolean contains(ItemStack item, int amount);

    /**
     * Find all slots in the inventory containing any ItemStacks with the given materialId
     *
     * @param materialId The materialId to look for
     * @return The Slots found.
     */
    public HashMap<Integer, ? extends ItemStack> all(int materialId);

    /**
     * Find all slots in the inventory containing any ItemStacks with the given material
     *
     * @param material The material to look for
     * @return The Slots found.
     */
    public HashMap<Integer, ? extends ItemStack> all(Material material);

    /**
     * Find all slots in the inventory containing any ItemStacks with the given ItemStack
     * This will only match slots if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return The Slots found.
     */
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item);

    /**
     * Find the first slot in the inventory containing an ItemStack with the given materialId
     *
     * @param materialId The materialId to look for
     * @return The Slot found.
     */
    public int first(int materialId);

    /**
     * Find the first slot in the inventory containing an ItemStack with the given material
     *
     * @param material The material to look for
     * @return The Slot found.
     */
    public int first(Material material);

    /**
     * Find the first slot in the inventory containing an ItemStack with the given stack
     * This will only match a slot if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return The Slot found.
     */
    public int first(ItemStack item);

    /**
     * Find the first empty Slot.
     *
     * @return The first empty Slot found, or -1 if no empty slots.
     */
    public int firstEmpty();

    /**
     * Remove all stacks in the inventory matching the given materialId.
     *
     * @param materialId The material to remove
     */
    public void remove(int materialId);

    /**
     * Remove all stacks in the inventory matching the given material.
     *
     * @param material The material to remove
     */
    public void remove(Material material);

    /**
     * Remove all stacks in the inventory matching the given stack.
     * This will only match a slot if both the type and the amount of the stack match
     *
     * @param item The ItemStack to match against
     */
    public void remove(ItemStack item);

    /**
     * Clear out a particular slot in the index
     *
     * @param index The index to empty.
     */
    public void clear(int index);

    /**
     * Clear out the whole index
     */
    public void clear();

    /**
     * Get a list of players viewing. Note that a player is considered to be viewing their own
     * inventory and internal crafting screen even when said inventory is not open. They will normally
     * be considered to be viewing their inventory even when they have a different inventory screen open,
     * but it's possible for customized inventory screens to exclude the viewer's inventory, so this should
     * never be assumed to be non-empty.
     * @return A list of players.
     */
    public List<HumanEntity> getViewers();

    /**
     * Get the title of this inventory.
     * @return The title.
     */
    public String getTitle();

    /**
     * Check what type of inventory this is.
     * @return The type of inventory.
     */
    public InventoryType getType();

    /**
     * Gets the block or entity belonging to the open inventory
     * @return The holder of the inventory; null if it has no holder.
     */
    public InventoryHolder getHolder();

    public ListIterator<ItemStack> iterator();

    /**
     * Returns an iterator starting at the given index. If the index is positive, then the first
     * call to next() will return the item at that index; if it is negative, the first call to
     * previous will return the item at index (getSize() + index).
     * @param index The index.
     * @return An iterator.
     */
    public ListIterator<ItemStack> iterator(int index);
}
