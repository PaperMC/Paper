package org.bukkit.inventory.view;

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * anvil view data.
 */
public interface AnvilView extends InventoryView {

    @NotNull
    @Override
    AnvilInventory getTopInventory();

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

    // Paper start - bypass anvil level restrictions
    /**
     * Returns whether this view will bypass the vanilla enchantment level restriction
     * when applying enchantments to an item or not.
     * <p>
     * By default, vanilla will limit enchantments applied to items to the respective
     * {@link org.bukkit.enchantments.Enchantment#getMaxLevel()}, even if the applied enchantment itself is above said
     * limit.
     * Disabling this limit via {@link AnvilView#bypassEnchantmentLevelRestriction(boolean)} allows for, e.g., enchanted
     * books to be applied fully, even if their enchantments are beyond the limit.
     *
     * @return {@code true} if this view bypasses the vanilla restrictions.
     */
    boolean bypassesEnchantmentLevelRestriction();

    /**
     * Sets if enchantments applied via this anvil view may bypass vanilla's level restrictions.
     *
     * @param bypassEnchantmentLevelRestriction if this view bypasses the vanilla level restrictions.
     * @see AnvilView#bypassesEnchantmentLevelRestriction()
     */
    void bypassEnchantmentLevelRestriction(boolean bypassEnchantmentLevelRestriction);
    // Paper end - bypass anvil level restrictions
}
