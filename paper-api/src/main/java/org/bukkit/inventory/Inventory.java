package org.bukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the various inventories. Behavior relating to {@link
 * Material#AIR} is unspecified.
 *
 * <br>
 * <b>Note that whilst {@link #iterator()} deals with the entire inventory, add
 * / contains / remove methods deal only with the storage contents.</b>
 * <br>
 * <b>Consider using {@link #getContents()} and {@link #getStorageContents()} for
 * specific iteration.</b>
 *
 * @see #getContents()
 * @see #getStorageContents()
 */
public interface Inventory extends Iterable<ItemStack> {

    /**
     * Returns the size of the inventory
     *
     * @return The size of the inventory
     */
    public int getSize();

    /**
     * Returns the maximum stack size for an ItemStack in this inventory.
     *
     * @return The maximum size for an ItemStack in this inventory.
     */
    public int getMaxStackSize();

    /**
     * This method allows you to change the maximum stack size for an
     * inventory.
     * <p>
     * <b>Caveats:</b>
     * <ul>
     * <li>Not all inventories respect this value.
     * <li>Stacks larger than 127 may be clipped when the world is saved.
     * <li>This value is not guaranteed to be preserved; be sure to set it
     *     before every time you want to set a slot over the max stack size.
     * <li>Stacks larger than the default max size for this type of inventory
     *     may not display correctly in the client.
     * </ul>
     *
     * @param size The new maximum stack size for items in this inventory.
     */
    public void setMaxStackSize(int size);

    /**
     * Returns the ItemStack found in the slot at the given index
     *
     * @param index The index of the Slot's ItemStack to return
     * @return The ItemStack in the slot
     */
    @Nullable
    public ItemStack getItem(int index);

    /**
     * Stores the ItemStack at the given index of the inventory.
     *
     * @param index The index where to put the ItemStack
     * @param item The ItemStack to set
     */
    public void setItem(int index, @Nullable ItemStack item);

    /**
     * Stores the given ItemStacks in the inventory. This will try to fill
     * existing stacks and empty slots as well as it can.
     * <p>
     * The returned HashMap contains what it couldn't store, where the key is
     * the index of the parameter, and the value is the ItemStack at that
     * index of the varargs parameter. If all items are stored, it will return
     * an empty HashMap.
     * <p>
     * If you pass in ItemStacks which exceed the maximum stack size for the
     * Material, first they will be added to partial stacks where
     * Material.getMaxStackSize() is not exceeded, up to
     * Material.getMaxStackSize(). When there are no partial stacks left
     * stacks will be split on Inventory.getMaxStackSize() allowing you to
     * exceed the maximum stack size for that material.
     * <p>
     * It is known that in some implementations this method will also set
     * the inputted argument amount to the number of that item not placed in
     * slots.
     *
     * @param items The ItemStacks to add
     * @return A HashMap containing items that didn't fit.
     * @throws IllegalArgumentException if items or any element in it is null
     */
    @NotNull
    public HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) throws IllegalArgumentException;

    /**
     * Removes the given ItemStacks from the inventory.
     * <p>
     * It will try to remove 'as much as possible' from the types and amounts
     * you give as arguments.
     * <p>
     * The returned HashMap contains what it couldn't remove, where the key is
     * the index of the parameter, and the value is the ItemStack at that
     * index of the varargs parameter. If all the given ItemStacks are
     * removed, it will return an empty HashMap.
     * <p>
     * It is known that in some implementations this method will also set the
     * inputted argument amount to the number of that item not removed from
     * slots.
     *
     * @param items The ItemStacks to remove
     * @return A HashMap containing items that couldn't be removed.
     * @throws IllegalArgumentException if items is null
     */
    @NotNull
    public HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... items) throws IllegalArgumentException;

    /**
     * Returns all ItemStacks from the inventory
     *
     * @return An array of ItemStacks from the inventory. Individual items may be null.
     */
    @NotNull
    public ItemStack[] getContents();

    /**
     * Completely replaces the inventory's contents. Removes all existing
     * contents and replaces it with the ItemStacks given in the array.
     *
     * @param items A complete replacement for the contents; the length must
     *     be less than or equal to {@link #getSize()}.
     * @throws IllegalArgumentException If the array has more items than the
     *     inventory.
     */
    public void setContents(@NotNull ItemStack[] items) throws IllegalArgumentException;

    /**
     * Return the contents from the section of the inventory where items can
     * reasonably be expected to be stored. In most cases this will represent
     * the entire inventory, but in some cases it may exclude armor or result
     * slots.
     * <br>
     * It is these contents which will be used for add / contains / remove
     * methods which look for a specific stack.
     *
     * @return inventory storage contents. Individual items may be null.
     */
    @NotNull
    public ItemStack[] getStorageContents();

    /**
     * Put the given ItemStacks into the storage slots
     *
     * @param items The ItemStacks to use as storage contents
     * @throws IllegalArgumentException If the array has more items than the
     * inventory.
     */
    public void setStorageContents(@NotNull ItemStack[] items) throws IllegalArgumentException;

    /**
     * Checks if the inventory contains any ItemStacks with the given
     * material.
     *
     * @param material The material to check for
     * @return true if an ItemStack is found with the given Material
     * @throws IllegalArgumentException if material is null
     */
    public boolean contains(@NotNull Material material) throws IllegalArgumentException;

    /**
     * Checks if the inventory contains any ItemStacks matching the given
     * ItemStack.
     * <p>
     * This will only return true if both the type and the amount of the stack
     * match.
     *
     * @param item The ItemStack to match against
     * @return false if item is null, true if any exactly matching ItemStacks
     *     were found
     */
    @Contract("null -> false")
    public boolean contains(@Nullable ItemStack item);

    /**
     * Checks if the inventory contains any ItemStacks with the given
     * material, adding to at least the minimum amount specified.
     *
     * @param material The material to check for
     * @param amount The minimum amount
     * @return true if amount is less than 1, true if enough ItemStacks were
     *     found to add to the given amount
     * @throws IllegalArgumentException if material is null
     */
    public boolean contains(@NotNull Material material, int amount) throws IllegalArgumentException;

    /**
     * Checks if the inventory contains at least the minimum amount specified
     * of exactly matching ItemStacks.
     * <p>
     * An ItemStack only counts if both the type and the amount of the stack
     * match.
     *
     * @param item the ItemStack to match against
     * @param amount how many identical stacks to check for
     * @return false if item is null, true if amount less than 1, true if
     *     amount of exactly matching ItemStacks were found
     * @see #containsAtLeast(ItemStack, int)
     */
    @Contract("null, _ -> false")
    public boolean contains(@Nullable ItemStack item, int amount);

    /**
     * Checks if the inventory contains ItemStacks matching the given
     * ItemStack whose amounts sum to at least the minimum amount specified.
     *
     * @param item the ItemStack to match against
     * @param amount the minimum amount
     * @return false if item is null, true if amount less than 1, true if
     *     enough ItemStacks were found to add to the given amount
     */
    @Contract("null, _ -> false")
    public boolean containsAtLeast(@Nullable ItemStack item, int amount);

    /**
     * Returns a HashMap with all slots and ItemStacks in the inventory with
     * the given Material.
     * <p>
     * The HashMap contains entries where, the key is the slot index, and the
     * value is the ItemStack in that slot. If no matching ItemStack with the
     * given Material is found, an empty map is returned.
     *
     * @param material The material to look for
     * @return A HashMap containing the slot index, ItemStack pairs
     * @throws IllegalArgumentException if material is null
     */
    @NotNull
    public HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException;

    /**
     * Finds all slots in the inventory containing any ItemStacks with the
     * given ItemStack. This will only match slots if both the type and the
     * amount of the stack match
     * <p>
     * The HashMap contains entries where, the key is the slot index, and the
     * value is the ItemStack in that slot. If no matching ItemStack with the
     * given Material is found, an empty map is returned.
     *
     * @param item The ItemStack to match against
     * @return A map from slot indexes to item at index
     */
    @NotNull
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item);

    /**
     * Finds the first slot in the inventory containing an ItemStack with the
     * given material
     *
     * @param material The material to look for
     * @return The slot index of the given Material or -1 if not found
     * @throws IllegalArgumentException if material is null
     */
    public int first(@NotNull Material material) throws IllegalArgumentException;

    /**
     * Returns the first slot in the inventory containing an ItemStack with
     * the given stack. This will only match a slot if both the type and the
     * amount of the stack match
     *
     * @param item The ItemStack to match against
     * @return The slot index of the given ItemStack or -1 if not found
     */
    public int first(@NotNull ItemStack item);

    /**
     * Returns the first empty Slot.
     *
     * @return The first empty Slot found, or -1 if no empty slots.
     */
    public int firstEmpty();

    /**
     * Check whether or not this inventory is empty. An inventory is considered
     * to be empty if there are no ItemStacks in any slot of this inventory.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Removes all stacks in the inventory matching the given material.
     *
     * @param material The material to remove
     * @throws IllegalArgumentException if material is null
     */
    public void remove(@NotNull Material material) throws IllegalArgumentException;

    /**
     * Removes all stacks in the inventory matching the given stack.
     * <p>
     * This will only match a slot if both the type and the amount of the
     * stack match
     *
     * @param item The ItemStack to match against
     */
    public void remove(@NotNull ItemStack item);

    /**
     * Clears out a particular slot in the index.
     *
     * @param index The index to empty.
     */
    public void clear(int index);

    /**
     * Clears out the whole Inventory.
     */
    public void clear();

    /**
     * Gets a list of players viewing the inventory. Note that a player is
     * considered to be viewing their own inventory and internal crafting
     * screen even when said inventory is not open. They will normally be
     * considered to be viewing their inventory even when they have a
     * different inventory screen open, but it's possible for customized
     * inventory screens to exclude the viewer's inventory, so this should
     * never be assumed to be non-empty.
     *
     * @return A list of HumanEntities who are viewing this Inventory.
     */
    @NotNull
    public List<HumanEntity> getViewers();

    /**
     * Returns what type of inventory this is.
     *
     * @return The InventoryType representing the type of inventory.
     */
    @NotNull
    public InventoryType getType();

    /**
     * Gets the block or entity belonging to the open inventory
     *
     * @return The holder of the inventory; null if it has no holder.
     */
    @Nullable
    public InventoryHolder getHolder();

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator();

    /**
     * Returns an iterator starting at the given index. If the index is
     * positive, then the first call to next() will return the item at that
     * index; if it is negative, the first call to previous will return the
     * item at index (getSize() + index).
     *
     * @param index The index.
     * @return An iterator.
     */
    @NotNull
    public ListIterator<ItemStack> iterator(int index);

    /**
     * Get the location of the block or entity which corresponds to this inventory. May return null if this container
     * was custom created or is a virtual / subcontainer.
     *
     * @return location or null if not applicable.
     */
    @Nullable
    public Location getLocation();
}
