package org.bukkit.inventory.meta;

import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

/**
 * EnchantmentMeta is specific to items that can <i>store</i> enchantments, as
 * opposed to being enchanted. {@link Material#ENCHANTED_BOOK} is an example
 * of an item with enchantment storage.
 */
public interface EnchantmentStorageMeta extends ItemMeta {

    /**
     * Checks for the existence of any stored enchantments.
     *
     * @return true if an enchantment exists on this meta
     */
    boolean hasStoredEnchants();

    /**
     * Checks for storage of the specified enchantment.
     *
     * @param enchant enchantment to check
     * @return true if this enchantment is stored in this meta
     */
    boolean hasStoredEnchant(@NotNull Enchantment enchant);

    /**
     * Checks for the level of the stored enchantment.
     *
     * @param enchant enchantment to check
     * @return The level that the specified stored enchantment has, or 0 if
     *     none
     */
    int getStoredEnchantLevel(@NotNull Enchantment enchant);

    /**
     * Gets a copy the stored enchantments in this ItemMeta.
     *
     * @return An immutable copy of the stored enchantments
     */
    @NotNull
    Map<Enchantment, Integer> getStoredEnchants();

    /**
     * Stores the specified enchantment in this item meta.
     *
     * @param enchant Enchantment to store
     * @param level Level for the enchantment
     * @param ignoreLevelRestriction this indicates the enchantment should be
     *     applied, ignoring the level limit
     * @return true if the item meta changed as a result of this call, false
     *     otherwise
     * @throws IllegalArgumentException if enchantment is null
     */
    boolean addStoredEnchant(@NotNull Enchantment enchant, int level, boolean ignoreLevelRestriction);

    /**
     * Remove the specified stored enchantment from this item meta.
     *
     * @param enchant Enchantment to remove
     * @return true if the item meta changed as a result of this call, false
     *     otherwise
     * @throws IllegalArgumentException if enchantment is null
     */
    boolean removeStoredEnchant(@NotNull Enchantment enchant) throws IllegalArgumentException;

    /**
     * Checks if the specified enchantment conflicts with any enchantments in
     * this ItemMeta.
     *
     * @param enchant enchantment to test
     * @return true if the enchantment conflicts, false otherwise
     */
    boolean hasConflictingStoredEnchant(@NotNull Enchantment enchant);

    @Override
    @NotNull
    EnchantmentStorageMeta clone();
}
