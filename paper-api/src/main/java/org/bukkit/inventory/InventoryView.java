package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;

/**
 * Represents a view linking two inventories and a single player
 * (whose inventory may or may not be one of the two)
 *
 * Note: If you implement this interface but fail to satisfy the expected
 * contracts of certain methods, there's no guarantee that the game
 * will work as it should.
 */
public abstract class InventoryView {
    public final static int OUTSIDE = -999;
    /**
     * Represents various extra properties of certai inventory windows.
     */
    public enum Property {
        /**
         * The progress of the down-pointing arrow in a brewing inventory.
         */
        BREW_TIME(0, InventoryType.BREWING),
        /**
         * The progress of the right-pointing arrow in a furnace inventory.
         */
        COOK_TIME(0, InventoryType.FURNACE),
        /**
         * The progress of the flame in a furnace inventory.
         */
        BURN_TIME(1, InventoryType.FURNACE),
        /**
         * How many total ticks the current fuel should last.
         */
        TICKS_FOR_CURRENT_FUEL(2, InventoryType.FURNACE),
        /**
         * In an enchanting inventory, the top button's experience level value.
         */
        ENCHANT_BUTTON1(0, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the middle button's experience level value.
         */
        ENCHANT_BUTTON2(1, InventoryType.ENCHANTING),
        /**
         * In an enchanting inventory, the bottom button's experience level value.
         */
        ENCHANT_BUTTON3(2, InventoryType.ENCHANTING);
        int id;
        InventoryType style;
        private Property(int id, InventoryType appliesTo) {
            this.id = id;
            style = appliesTo;
        }

        public InventoryType getType() {
            return style;
        }

        public int getId() {
            return id;
        }
    }
    /**
     * Get the upper inventory involved in this transaction.
     * @return the inventory
     */
    public abstract Inventory getTopInventory();

    /**
     * Get the lower inventory involved in this transaction.
     * @return the inventory
     */
    public abstract Inventory getBottomInventory();

    /**
     * Get the player viewing.
     * @return the player
     */
    public abstract HumanEntity getPlayer();

    /**
     * Determine the type of inventory involved in the transaction. This indicates
     * the window style being shown. It will never return PLAYER, since that is
     * common to all windows.
     * @return the inventory type
     */
    public abstract InventoryType getType();

    /**
     * Sets one item in this inventory view by its raw slot ID.
     * <p />
     * Note: If slot ID -999 is chosen, it may be expected that the item is
     * dropped on the ground. This is not required behaviour, however.
     * @param slot The ID as returned by InventoryClickEvent.getRawSlot()
     * @param item The new item to put in the slot, or null to clear it.
     */
    public void setItem(int slot, ItemStack item) {
        if (slot != OUTSIDE) {
            if (slot < getTopInventory().getSize()) {
                getTopInventory().setItem(convertSlot(slot),item);
            } else {
                getBottomInventory().setItem(convertSlot(slot),item);
            }
        } else {
            getPlayer().getWorld().dropItemNaturally(getPlayer().getLocation(), item);
        }
    }

    /**
     * Sets one item in this inventory view by its raw slot ID.
     * @param slot The ID as returned by InventoryClickEvent.getRawSlot()
     * @return The item currently in the slot.
     */
    public ItemStack getItem(int slot) {
        if (slot == OUTSIDE) {
            return null;
        }
        if (slot < getTopInventory().getSize()) {
            return getTopInventory().getItem(convertSlot(slot));
        } else {
            return getBottomInventory().getItem(convertSlot(slot));
        }
    }

    /**
     * Sets the item on the cursor of one of the viewing players.
     * @param item The item to put on the cursor, or null to remove the item on their cursor.
     */
    public final void setCursor(ItemStack item) {
        getPlayer().setItemOnCursor(item);
    }

    /**
     * Get the item on the cursor of one of the viewing players.
     * @return The item on the player's cursor, or null if they aren't holding one.
     */
    public final ItemStack getCursor() {
        return getPlayer().getItemOnCursor();
    }

    /**
     * Converts a raw slot ID into its local slot ID into whichever of the two inventories
     * the slot points to. If the raw slot refers to the upper inventory, it will be returned
     * unchanged and thus be suitable for getTopInventory().getItem(); if it refers to the
     * lower inventory, the output will differ from the input and be suitable for
     * getBottomInventory().getItem().
     * @param rawSlot The raw slot ID.
     * @return The converted slot ID.
     */
    public final int convertSlot(int rawSlot) {
        int numInTop = getTopInventory().getSize();
        if (rawSlot < numInTop) {
            return rawSlot;
        }
        int slot = rawSlot - numInTop;
        if (getType() == InventoryType.CRAFTING) {
            if(slot < 4) return 39 - slot;
            else slot -= 4;
        }
        if (slot >= 27) slot -= 27;
        else slot += 9;
        return slot;
    }

    /**
     * Closes the inventory view.
     */
    public final void close() {
        getPlayer().closeInventory();
    }

    /**
     * Check the total number of slots in this view, combining the upper and lower inventories.
     * Note though that it's possible for this to be greater than the sum of the two inventories
     * if for example some slots are not being used.
     * @return The total size
     */
    public final int countSlots() {
        return getTopInventory().getSize() + getBottomInventory().getSize();
    }

    public final boolean setProperty(Property prop, int value) {
        return getPlayer().setWindowProperty(prop, value);
    }

    /**
     * Get the title of this inventory window.
     * @return The title.
     */
    public final String getTitle() {
        return getTopInventory().getTitle();
    }
}
