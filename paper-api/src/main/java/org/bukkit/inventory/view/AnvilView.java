package org.bukkit.inventory.view;

import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.Nullable;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * anvil view data.
 */
public interface AnvilView extends InventoryView {

    /**
     * Gets the rename text specified within the anvil's text field.
     *
     * @return The text within the anvil's text field if an item is present
     * otherwise null
     */
    @Nullable
    String getRenameText();

    /**
     * Gets the amount of items needed to repair.
     *
     * @return The amount of materials required to repair the item
     */
    int getRepairItemCountCost();

    /**
     * Gets the experience cost needed to repair.
     *
     * @return The repair cost in experience
     */
    int getRepairCost();

    /**
     * Gets the maximum repair cost needed to repair.
     *
     * @return The maximum repair cost in experience
     */
    int getMaximumRepairCost();

    /**
     * Sets the amount of repair materials required to repair the item.
     *
     * @param amount the amount of repair materials
     */
    void setRepairItemCountCost(int amount);

    /**
     * Sets the repair cost in experience.
     *
     * @param cost the experience cost to repair
     */
    void setRepairCost(int cost);

    /**
     * Sets maximum repair cost in experience.
     *
     * @param levels the levels to set
     */
    void setMaximumRepairCost(int levels);
}
