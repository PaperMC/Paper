package org.bukkit.inventory;

import org.bukkit.inventory.view.AnvilView;
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
     * @deprecated use {@link AnvilView#getRenameText()}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    @Nullable
    String getRenameText();

    /**
     * Get the item cost (in amount) to complete the current repair.
     *
     * @return the amount
     * @deprecated use {@link AnvilView#getRepairItemCountCost()}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    int getRepairCostAmount();

    /**
     * Set the item cost (in amount) to complete the current repair.
     *
     * @param amount the amount
     * @deprecated use {@link AnvilView#setRepairItemCountCost(int)}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    void setRepairCostAmount(int amount);

    /**
     * Get the experience cost (in levels) to complete the current repair.
     *
     * @return the experience cost
     * @deprecated use {@link AnvilView#getRepairCost()}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    int getRepairCost();

    /**
     * Set the experience cost (in levels) to complete the current repair.
     *
     * @param levels the experience cost
     * @deprecated use {@link AnvilView#setRepairCost(int)}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    void setRepairCost(int levels);

    /**
     * Get the maximum experience cost (in levels) to be allowed by the current
     * repair. If the result of {@link #getRepairCost()} exceeds the returned
     * value, the repair result will be air due to being "too expensive".
     * <p>
     * By default, this level is set to 40. Players in creative mode ignore the
     * maximum repair cost.
     *
     * @return the maximum experience cost
     * @deprecated use {@link AnvilView#getMaximumRepairCost()}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    int getMaximumRepairCost();

    /**
     * Set the maximum experience cost (in levels) to be allowed by the current
     * repair. The default value set by vanilla Minecraft is 40.
     *
     * @param levels the maximum experience cost
     * @deprecated use {@link AnvilView#setMaximumRepairCost(int)}.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    void setMaximumRepairCost(int levels);

    // Paper start
    /**
     * Gets the item in the left input slot.
     *
     * @return item in the first slot
     */
    @Nullable
    default ItemStack getFirstItem() {
        return getItem(0);
    }

    /**
     * Sets the item in the left input slot.
     *
     * @param firstItem item to set
     */
    default void setFirstItem(@Nullable ItemStack firstItem) {
        setItem(0, firstItem);
    }

    /**
     * Gets the item in the right input slot.
     *
     * @return item in the second slot
     */
    @Nullable
    default ItemStack getSecondItem() {
        return getItem(1);
    }

    /**
     * Sets the item in the right input slot.
     *
     * @param secondItem item to set
     */
    default void setSecondItem(@Nullable ItemStack secondItem) {
        setItem(1, secondItem);
    }

    /**
     * Gets the item in the result slot.
     *
     * @return item in the result slot
     */
    @Nullable
    default ItemStack getResult() {
        return getItem(2);
    }

    /**
     * Sets the item in the result slot.
     * Note that the client might not be able to take out the item if it does not match the input items.
     *
     * @param result item to set
     */
    default void setResult(@Nullable ItemStack result) {
        setItem(2, result);
    }
    // Paper end
}
