package org.bukkit.inventory;

import org.jetbrains.annotations.Nullable;

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
    @Nullable
    String getRenameText();

    /**
     * Get the item cost (in amount) to complete the current repair.
     *
     * @return the amount
     */
    int getRepairCostAmount();

    /**
     * Set the item cost (in amount) to complete the current repair.
     *
     * @param amount the amount
     */
    void setRepairCostAmount(int amount);

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

    /**
     * Get the maximum experience cost (in levels) to be allowed by the current
     * repair. If the result of {@link #getRepairCost()} exceeds the returned
     * value, the repair result will be air to due being "too expensive".
     * <p>
     * By default, this level is set to 40. Players in creative mode ignore the
     * maximum repair cost.
     *
     * @return the maximum experience cost
     */
    int getMaximumRepairCost();

    /**
     * Set the maximum experience cost (in levels) to be allowed by the current
     * repair. The default value set by vanilla Minecraft is 40.
     *
     * @param levels the maximum experience cost
     */
    void setMaximumRepairCost(int levels);
}
