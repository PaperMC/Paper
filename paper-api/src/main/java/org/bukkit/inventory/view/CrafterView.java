package org.bukkit.inventory.view;

import org.bukkit.inventory.InventoryView;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * crafter view data.
 */
public interface CrafterView extends InventoryView {

    /**
     * Checks if the given crafter slot is disabled.
     *
     * @param slot the slot to check
     * @return true if the slot is disabled otherwise false
     */
    boolean isSlotDisabled(int slot);

    /**
     * Checks whether or not this crafter view is powered.
     *
     * @return true if the crafter is powered
     */
    boolean isPowered();

    /**
     * Sets the status of the crafter slot.
     *
     * @param slot the slot to set the status of
     * @param disabled true if the slot should be disabled otherwise false
     */
    void setSlotDisabled(int slot, boolean disabled);
}
