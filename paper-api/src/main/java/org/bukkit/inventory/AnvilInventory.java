package org.bukkit.inventory;

/**
 * Interface to the inventory of an Anvil.
 */
public interface AnvilInventory extends Inventory {

    /**
     * Get the name to be applied to the repaired item. An empty string denotes
     * the default item name.
     *
     * @return the rename text
     */
    String getRenameText();

    /**
     * Get the experience cost (in levels) to complete the current repair.
     *
     * @return the experience cost
     */
    int getRepairCost();

    /**
     * Set the experience cost (in levels) to complete the current repair.
     *
     * @param levels the experience cost
     */
    void setRepairCost(int levels);
}
