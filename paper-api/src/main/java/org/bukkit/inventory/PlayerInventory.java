package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface to the inventory of a Player, including the four armor slots and any extra slots.
 */
public interface PlayerInventory extends Inventory {

    /**
     * Get all ItemStacks from the armor slots
     *
     * @return All the ItemStacks from the armor slots. Individual items can be null.
     */
    @NotNull
    public ItemStack[] getArmorContents();

    /**
     * Get all additional ItemStacks stored in this inventory.
     * <br>
     * NB: What defines an extra slot is up to the implementation, however it
     * will not be contained within {@link #getStorageContents()} or
     * {@link #getArmorContents()}
     *
     * @return All additional ItemStacks. Individual items can be null.
     */
    @NotNull
    public ItemStack[] getExtraContents();

    /**
     * Return the ItemStack from the helmet slot
     *
     * @return The ItemStack in the helmet slot
     */
    @Nullable
    public ItemStack getHelmet();

    /**
     * Return the ItemStack from the chestplate slot
     *
     * @return The ItemStack in the chestplate slot
     */
    @Nullable
    public ItemStack getChestplate();

    /**
     * Return the ItemStack from the leg slot
     *
     * @return The ItemStack in the leg slot
     */
    @Nullable
    public ItemStack getLeggings();

    /**
     * Return the ItemStack from the boots slot
     *
     * @return The ItemStack in the boots slot
     */
    @Nullable
    public ItemStack getBoots();

    /**
     * Stores the ItemStack at the given index of the inventory.
     * <p>
     * Indexes 0 through 8 refer to the hotbar. 9 through 35 refer to the main inventory, counting up from 9 at the top
     * left corner of the inventory, moving to the right, and moving to the row below it back on the left side when it
     * reaches the end of the row. It follows the same path in the inventory like you would read a book.
     * <p>
     * Indexes 36 through 39 refer to the armor slots. Though you can set armor with this method using these indexes,
     * you are encouraged to use the provided methods for those slots.
     * <p>
     * Index 40 refers to the off hand (shield) item slot. Though you can set off hand with this method using this index,
     * you are encouraged to use the provided method for this slot.
     * <p>
     * If you attempt to use this method with an index less than 0 or greater than 40, an ArrayIndexOutOfBounds
     * exception will be thrown.
     *
     * @param index The index where to put the ItemStack
     * @param item The ItemStack to set
     * @throws ArrayIndexOutOfBoundsException when index &lt; 0 || index &gt; 40
     * @see #setBoots(ItemStack)
     * @see #setChestplate(ItemStack)
     * @see #setHelmet(ItemStack)
     * @see #setLeggings(ItemStack)
     * @see #setItemInOffHand(ItemStack)
     */
    @Override
    public void setItem(int index, @Nullable ItemStack item);

    /**
     * Stores the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to put the ItemStack
     * @param item the ItemStack to set
     *
     * @see #setItem(int, ItemStack)
     */
    public void setItem(@NotNull EquipmentSlot slot, @Nullable ItemStack item);

    /**
     * Gets the ItemStack at the given equipment slot in the inventory.
     *
     * @param slot the slot to get the ItemStack
     *
     * @return the ItemStack in the given slot or null if there is not one
     */
    @Nullable
    public ItemStack getItem(@NotNull EquipmentSlot slot);

    /**
     * Put the given ItemStacks into the armor slots
     *
     * @param items The ItemStacks to use as armour
     */
    public void setArmorContents(@Nullable ItemStack[] items);

    /**
     * Put the given ItemStacks into the extra slots
     * <br>
     * See {@link #getExtraContents()} for an explanation of extra slots.
     *
     * @param items The ItemStacks to use as extra
     */
    public void setExtraContents(@Nullable ItemStack[] items);

    /**
     * Put the given ItemStack into the helmet slot. This does not check if
     * the ItemStack is a helmet
     *
     * @param helmet The ItemStack to use as helmet
     */
    public void setHelmet(@Nullable ItemStack helmet);

    /**
     * Put the given ItemStack into the chestplate slot. This does not check
     * if the ItemStack is a chestplate
     *
     * @param chestplate The ItemStack to use as chestplate
     */
    public void setChestplate(@Nullable ItemStack chestplate);

    /**
     * Put the given ItemStack into the leg slot. This does not check if the
     * ItemStack is a pair of leggings
     *
     * @param leggings The ItemStack to use as leggings
     */
    public void setLeggings(@Nullable ItemStack leggings);

    /**
     * Put the given ItemStack into the boots slot. This does not check if the
     * ItemStack is a boots
     *
     * @param boots The ItemStack to use as boots
     */
    public void setBoots(@Nullable ItemStack boots);

    /**
     * Gets a copy of the item the player is currently holding
     * in their main hand.
     *
     * @return the currently held item
     */
    @NotNull
    ItemStack getItemInMainHand();

    /**
     * Sets the item the player is holding in their main hand.
     *
     * @param item The item to put into the player's hand
     */
    void setItemInMainHand(@Nullable ItemStack item);

    /**
     * Gets a copy of the item the player is currently holding
     * in their off hand.
     *
     * @return the currently held item
     */
    @NotNull
    ItemStack getItemInOffHand();

    /**
     * Sets the item the player is holding in their off hand.
     *
     * @param item The item to put into the player's hand
     */
    void setItemInOffHand(@Nullable ItemStack item);

    /**
     * Gets a copy of the item the player is currently holding
     *
     * @return the currently held item
     * @see #getItemInMainHand()
     * @see #getItemInOffHand()
     * @deprecated players can duel wield now use the methods for the
     *      specific hand instead
     */
    @Deprecated
    @NotNull
    public ItemStack getItemInHand();

    /**
     * Sets the item the player is holding
     *
     * @param stack The item to put into the player's hand
     * @see #setItemInMainHand(ItemStack)
     * @see #setItemInOffHand(ItemStack)
     * @deprecated players can duel wield now use the methods for the
     *      specific hand instead
     */
    @Deprecated
    public void setItemInHand(@Nullable ItemStack stack);

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

    @Override
    @Nullable
    public HumanEntity getHolder();
}
