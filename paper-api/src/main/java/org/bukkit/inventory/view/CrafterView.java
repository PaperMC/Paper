package org.bukkit.inventory.view;

import org.bukkit.inventory.CrafterInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * crafter view data.
 */
public interface CrafterView extends InventoryView {

    @NotNull
    @Override
    CrafterInventory getTopInventory();

    /**
     * Checks if the given crafter slot is disabled.
     *
     * @param slot the slot to check
     * @return whether the slot is disabled
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
     * @param disabled whether the slot should be disabled
     */
    void setSlotDisabled(int slot, boolean disabled);
}
