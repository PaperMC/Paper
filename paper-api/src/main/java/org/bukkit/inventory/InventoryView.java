package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a view linking two inventories and a single player (whose
 * inventory may or may not be one of the two).
 */
public interface InventoryView {
    public static final int OUTSIDE = -999;
    /**
     * Represents various extra properties of certain inventory windows.
     * @deprecated use {@link InventoryView} and its children
     */
    @Deprecated(forRemoval = true, since = "1.21")
    public enum Property {
        /**
         * The progress of the down-pointing arrow in a brewing inventory.
         */
        BREW_TIME(0, InventoryType.BREWING),
        /**
         * The progress of the fuel slot in a brewing inventory.
         *
         * This is a value between 0 and 20, with 0 making the bar empty, and 20
         * making the bar full.
         */
        FUEL_TIME(1, InventoryType.BREWING),
        /**
         * The progress of the flame in a furnace inventory.
         */
        BURN_TIME(0, InventoryType.FURNACE),
        /**
         * How many total ticks the current fuel should last.
         */
        TICKS_FOR_CURRENT_FUEL(1, InventoryType.FURNACE),
        /**
         * The progress of the right-pointing arrow in a furnace inventory.
         */
        COOK_TIME(2, InventoryType.FURNACE),
        /**
         * How many total ticks the current smelting should last.
         */
        TICKS_FOR_CURRENT_SMELTING(3, InventoryType.FURNACE),
        /**
         * In an enchanting inventory, the top button's experience level
         * value.
         */
        ENCHANT_BUTTON1(0, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the middle button's experience level
         * value.
         */
        ENCHANT_BUTTON2(1, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the bottom button's experience level
         * value.
         */
        ENCHANT_BUTTON3(2, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the first four bits of the player's xpSeed.
         */
        ENCHANT_XP_SEED(3, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the top button's enchantment's id
         */
        ENCHANT_ID1(4, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the middle button's enchantment's id
         */
        ENCHANT_ID2(5, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the bottom button's enchantment's id
         */
        ENCHANT_ID3(6, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the top button's level value.
         */
        ENCHANT_LEVEL1(7, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the middle button's level value.
         */
        ENCHANT_LEVEL2(8, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the bottom button's level value.
         */
        ENCHANT_LEVEL3(9, InventoryType.ENCHANTING),
        /**
         * In an beacon inventory, the levels of the beacon
         */
        LEVELS(0, InventoryType.BEACON),
        /**
         * In an beacon inventory, the primary potion effect
         */
        PRIMARY_EFFECT(1, InventoryType.BEACON),
        /**
         * In an beacon inventory, the secondary potion effect
         */
        SECONDARY_EFFECT(2, InventoryType.BEACON),
        /**
         * The repair's cost in xp levels
         */
        REPAIR_COST(0, InventoryType.ANVIL),
        /**
         * The lectern's current open book page
         */
        BOOK_PAGE(0, InventoryType.LECTERN);
        int id;
        InventoryType style;
        private Property(int id, /*@NotNull*/ InventoryType appliesTo) {
            this.id = id;
            style = appliesTo;
        }

        @NotNull
        public InventoryType getType() {
            return style;
        }

        /**
         * Gets the id of this view.
         *
         * @return the id of this view
         * @apiNote Internal Use Only
         */
        @org.jetbrains.annotations.ApiStatus.Internal // Paper
        public int getId() {
            return id;
        }
    }
    /**
     * Get the upper inventory involved in this transaction.
     *
     * @return the inventory
     */
    @NotNull
    public Inventory getTopInventory();

    /**
     * Get the lower inventory involved in this transaction.
     *
     * @return the inventory
     */
    @NotNull
    public Inventory getBottomInventory();

    /**
     * Get the player viewing.
     *
     * @return the player
     */
    @NotNull
    public HumanEntity getPlayer();

    /**
     * Determine the type of inventory involved in the transaction. This
     * indicates the window style being shown. It will never return PLAYER,
     * since that is common to all windows.
     *
     * @return the inventory type
     */
    @NotNull
    public InventoryType getType();

    /**
     * Sets one item in this inventory view by its raw slot ID.
     * <p>
     * Note: If slot ID -999 is chosen, it may be expected that the item is
     * dropped on the ground. This is not required behaviour, however.
     *
     * @param slot The ID as returned by InventoryClickEvent.getRawSlot()
     * @param item The new item to put in the slot, or null to clear it.
     */
    public void setItem(int slot, @Nullable ItemStack item);

    /**
     * Gets one item in this inventory view by its raw slot ID.
     *
     * @param slot The ID as returned by InventoryClickEvent.getRawSlot()
     * @return The item currently in the slot.
     */
    @Nullable
    public ItemStack getItem(int slot);

    /**
     * Sets the item on the cursor of one of the viewing players.
     *
     * @param item The item to put on the cursor, or null to remove the item
     *     on their cursor.
     */
    public void setCursor(@Nullable ItemStack item);

    /**
     * Get the item on the cursor of one of the viewing players.
     *
     * @return The item on the player's cursor, or an empty stack
     * if they aren't holding one.
     */
    @NotNull // Paper - fix nullability
    public ItemStack getCursor();

    /**
     * Gets the inventory corresponding to the given raw slot ID.
     *
     * If the slot ID is {@link #OUTSIDE} null will be returned, otherwise
     * behaviour for illegal and negative slot IDs is undefined.
     *
     * May be used with {@link #convertSlot(int)} to directly index an
     * underlying inventory.
     *
     * @param rawSlot The raw slot ID.
     * @return corresponding inventory, or null
     */
    @Nullable
    public Inventory getInventory(int rawSlot);

    /**
     * Converts a raw slot ID into its local slot ID into whichever of the two
     * inventories the slot points to.
     * <p>
     * If the raw slot refers to the upper inventory, it will be returned
     * unchanged and thus be suitable for getTopInventory().getItem(); if it
     * refers to the lower inventory, the output will differ from the input
     * and be suitable for getBottomInventory().getItem().
     *
     * @param rawSlot The raw slot ID.
     * @return The converted slot ID.
     */
    public int convertSlot(int rawSlot);

    /**
     * Determine the type of the slot by its raw slot ID.
     * <p>
     * If the type of the slot is unknown, then
     * {@link InventoryType.SlotType#CONTAINER} will be returned.
     *
     * @param slot The raw slot ID
     * @return the slot type
     */
    @NotNull
    public InventoryType.SlotType getSlotType(int slot);

    /**
     * Opens the inventory view.
     */
    void open();

    /**
     * Closes the inventory view.
     */
    public void close();

    /**
     * Check the total number of slots in this view, combining the upper and
     * lower inventories.
     * <p>
     * Note though that it's possible for this to be greater than the sum of
     * the two inventories if for example some slots are not being used.
     *
     * @return The total size
     */
    public int countSlots();

    /**
     * Sets an extra property of this inventory if supported by that
     * inventory, for example the state of a progress bar.
     *
     * @param prop the window property to update
     * @param value the new value for the window property
     * @return true if the property was updated successfully, false if the
     *     property is not supported by that inventory
     */
    public boolean setProperty(@NotNull Property prop, int value);

    // Paper start
    /**
     * Get the title of this inventory window.
     *
     * @return The title.
     */
    @NotNull
    default net.kyori.adventure.text.Component title() {
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(this.getTitle());
    }
    // Paper end

    /**
     * Get the title of this inventory window.
     *
     * @return The title.
     * @deprecated in favour of {@link #title()}
     */
    @Deprecated // Paper
    @NotNull
    public String getTitle();

    /**
     * Get the original title of this inventory window, before any changes were
     * made using {@link #setTitle(String)}.
     *
     * @return the original title
     * @deprecated changing the title is not supported
     */
    @NotNull
    @Deprecated(since = "1.21.1") // Paper
    public String getOriginalTitle();

    /**
     * Sets the title of this inventory window to the specified title if the
     * inventory window supports it.
     * <p>
     * Note if the inventory does not support titles that can be changed (ie, it
     * is not creatable or viewed by a player), then this method will throw an
     * exception.
     *
     * @param title The new title.
     * @deprecated changing the title is not supported. This method has
     * poorly defined and broken behaviors. It should not be used.
     */
    @Deprecated(since = "1.21.1") // Paper
    public void setTitle(@NotNull String title);

    /**
     * Gets the menu type of the inventory view if applicable.
     * <p>
     * Some inventory types do not support a menu type. In such cases, this method
     * returns null. This typically applies to inventories belonging to entities
     * like players or animals (e.g., a horse).
     *
     * @return the menu type of the inventory view or null if not applicable
     */
    @ApiStatus.Experimental
    @Nullable MenuType getMenuType();
}
