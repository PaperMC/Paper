package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a view linking two inventories and a single player (whose
 * inventory may or may not be one of the two).
 * <p>
 * Note: If you implement this interface but fail to satisfy the expected
 * contracts of certain methods, there's no guarantee that the game will work
 * as it should.
 */
public abstract class InventoryView {
    public static final int OUTSIDE = -999;
    /**
     * Represents various extra properties of certain inventory windows.
     */
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
         * @deprecated Magic value
         */
        @Deprecated
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
    public abstract Inventory getTopInventory();

    /**
     * Get the lower inventory involved in this transaction.
     *
     * @return the inventory
     */
    @NotNull
    public abstract Inventory getBottomInventory();

    /**
     * Get the player viewing.
     *
     * @return the player
     */
    @NotNull
    public abstract HumanEntity getPlayer();

    /**
     * Determine the type of inventory involved in the transaction. This
     * indicates the window style being shown. It will never return PLAYER,
     * since that is common to all windows.
     *
     * @return the inventory type
     */
    @NotNull
    public abstract InventoryType getType();

    /**
     * Sets one item in this inventory view by its raw slot ID.
     * <p>
     * Note: If slot ID -999 is chosen, it may be expected that the item is
     * dropped on the ground. This is not required behaviour, however.
     *
     * @param slot The ID as returned by InventoryClickEvent.getRawSlot()
     * @param item The new item to put in the slot, or null to clear it.
     */
    public void setItem(int slot, @Nullable ItemStack item) {
        Inventory inventory = getInventory(slot);
        if (inventory != null) {
            inventory.setItem(convertSlot(slot), item);
        } else if (item != null) {
            getPlayer().getWorld().dropItemNaturally(getPlayer().getLocation(), item);
        }
    }

    /**
     * Gets one item in this inventory view by its raw slot ID.
     *
     * @param slot The ID as returned by InventoryClickEvent.getRawSlot()
     * @return The item currently in the slot.
     */
    @Nullable
    public ItemStack getItem(int slot) {
        Inventory inventory = getInventory(slot);
        return (inventory == null) ? null : inventory.getItem(convertSlot(slot));
    }

    /**
     * Sets the item on the cursor of one of the viewing players.
     *
     * @param item The item to put on the cursor, or null to remove the item
     *     on their cursor.
     */
    public final void setCursor(@Nullable ItemStack item) {
        getPlayer().setItemOnCursor(item);
    }

    /**
     * Get the item on the cursor of one of the viewing players.
     *
     * @return The item on the player's cursor, or null if they aren't holding
     *     one.
     */
    @Nullable
    public final ItemStack getCursor() {
        return getPlayer().getItemOnCursor();
    }

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
    public final Inventory getInventory(int rawSlot) {
        // Slot may be -1 if not properly detected due to client bug
        // e.g. dropping an item into part of the enchantment list section of an enchanting table
        if (rawSlot == OUTSIDE || rawSlot == -1) {
            return null;
        }
        Preconditions.checkArgument(rawSlot >= 0, "Negative, non outside slot %s", rawSlot);
        Preconditions.checkArgument(rawSlot < countSlots(), "Slot %s greater than inventory slot count", rawSlot);

        if (rawSlot < getTopInventory().getSize()) {
            return getTopInventory();
        } else {
            return getBottomInventory();
        }
    }

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
    public final int convertSlot(int rawSlot) {
        int numInTop = getTopInventory().getSize();
        // Index from the top inventory as having slots from [0,size]
        if (rawSlot < numInTop) {
            return rawSlot;
        }

        // Move down the slot index by the top size
        int slot = rawSlot - numInTop;

        // Player crafting slots are indexed differently. The matrix is caught by the first return.
        // Creative mode is the same, except that you can't see the crafting slots (but the IDs are still used)
        if (getType() == InventoryType.CRAFTING || getType() == InventoryType.CREATIVE) {
            /*
             * Raw Slots:
             *
             * 5             1  2     0
             * 6             3  4
             * 7
             * 8           45
             * 9  10 11 12 13 14 15 16 17
             * 18 19 20 21 22 23 24 25 26
             * 27 28 29 30 31 32 33 34 35
             * 36 37 38 39 40 41 42 43 44
             */

            /*
             * Converted Slots:
             *
             * 39             1  2     0
             * 38             3  4
             * 37
             * 36          40
             * 9  10 11 12 13 14 15 16 17
             * 18 19 20 21 22 23 24 25 26
             * 27 28 29 30 31 32 33 34 35
             * 0  1  2  3  4  5  6  7  8
             */

            if (slot < 4) {
                // Send [5,8] to [39,36]
                return 39 - slot;
            } else if (slot > 39) {
                // Slot lives in the extra slot section
                return slot;
            } else {
                // Reset index so 9 -> 0
                slot -= 4;
            }
        }

        // 27 = 36 - 9
        if (slot >= 27) {
            // Put into hotbar section
            slot -= 27;
        } else {
            // Take out of hotbar section
            // 9 = 36 - 27
            slot += 9;
        }

        return slot;
    }

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
    public final InventoryType.SlotType getSlotType(int slot) {
        InventoryType.SlotType type = InventoryType.SlotType.CONTAINER;
        if (slot >= 0 && slot < this.getTopInventory().getSize()) {
            switch (this.getType()) {
            case BLAST_FURNACE:
            case FURNACE:
            case SMOKER:
                if (slot == 2) {
                    type = InventoryType.SlotType.RESULT;
                } else if (slot == 1) {
                    type = InventoryType.SlotType.FUEL;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;
            case BREWING:
                if (slot == 3) {
                    type = InventoryType.SlotType.FUEL;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;
            case ENCHANTING:
                type = InventoryType.SlotType.CRAFTING;
                break;
            case WORKBENCH:
            case CRAFTING:
                if (slot == 0) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;
            case BEACON:
                type = InventoryType.SlotType.CRAFTING;
                break;
            case ANVIL:
            case SMITHING:
            case CARTOGRAPHY:
            case GRINDSTONE:
            case MERCHANT:
                if (slot == 2) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;
            case STONECUTTER:
                if (slot == 1) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;
            case LOOM:
            case SMITHING_NEW:
                if (slot == 3) {
                    type = InventoryType.SlotType.RESULT;
                } else {
                    type = InventoryType.SlotType.CRAFTING;
                }
                break;
            default:
                // Nothing to do, it's a CONTAINER slot
            }
        } else {
            if (slot < 0) {
                type = InventoryType.SlotType.OUTSIDE;
            } else if (this.getType() == InventoryType.CRAFTING) { // Also includes creative inventory
                if (slot < 9) {
                    type = InventoryType.SlotType.ARMOR;
                } else if (slot > 35) {
                    type = InventoryType.SlotType.QUICKBAR;
                }
            } else if (slot >= (this.countSlots() - (9 + 4 + 1))) { // Quickbar, Armor, Offhand
                type = InventoryType.SlotType.QUICKBAR;
            }
        }
        return type;
    }

    /**
     * Closes the inventory view.
     */
    public final void close() {
        getPlayer().closeInventory();
    }

    /**
     * Check the total number of slots in this view, combining the upper and
     * lower inventories.
     * <p>
     * Note though that it's possible for this to be greater than the sum of
     * the two inventories if for example some slots are not being used.
     *
     * @return The total size
     */
    public final int countSlots() {
        return getTopInventory().getSize() + getBottomInventory().getSize();
    }

    /**
     * Sets an extra property of this inventory if supported by that
     * inventory, for example the state of a progress bar.
     *
     * @param prop the window property to update
     * @param value the new value for the window property
     * @return true if the property was updated successfully, false if the
     *     property is not supported by that inventory
     */
    public final boolean setProperty(@NotNull Property prop, int value) {
        return getPlayer().setWindowProperty(prop, value);
    }

    /**
     * Get the title of this inventory window.
     *
     * @return The title.
     */
    @NotNull
    public abstract String getTitle();

    /**
     * Get the original title of this inventory window, before any changes were
     * made using {@link #setTitle(String)}.
     *
     * @return the original title
     */
    @NotNull
    public abstract String getOriginalTitle();

    /**
     * Sets the title of this inventory window to the specified title if the
     * inventory window supports it.
     * <p>
     * Note if the inventory does not support titles that can be changed (ie, it
     * is not creatable or viewed by a player), then this method will throw an
     * exception.
     *
     * @param title The new title.
     */
    public abstract void setTitle(@NotNull String title);
}
