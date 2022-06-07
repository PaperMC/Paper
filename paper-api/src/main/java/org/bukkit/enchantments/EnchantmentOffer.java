package org.bukkit.enchantments;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * A class for the available enchantment offers in the enchantment table.
 */
public class EnchantmentOffer {

    private Enchantment enchantment;
    private int enchantmentLevel;
    private int cost;

    public EnchantmentOffer(@NotNull Enchantment enchantment, int enchantmentLevel, int cost) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
        this.cost = cost;
    }

    /**
     * Get the type of the enchantment.
     *
     * @return type of enchantment
     */
    @NotNull
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * Sets the type of the enchantment.
     *
     * @param enchantment type of the enchantment
     */
    public void setEnchantment(@NotNull Enchantment enchantment) {
        Preconditions.checkArgument(enchantment != null, "The enchantment may not be null!");

        this.enchantment = enchantment;
    }

    /**
     * Gets the level of the enchantment.
     *
     * @return level of the enchantment
     */
    public int getEnchantmentLevel() {
        return enchantmentLevel;
    }

    /**
     * Sets the level of the enchantment.
     *
     * @param enchantmentLevel level of the enchantment
     */
    public void setEnchantmentLevel(int enchantmentLevel) {
        Preconditions.checkArgument(enchantmentLevel > 0, "The enchantment level must be greater than 0!");

        this.enchantmentLevel = enchantmentLevel;
    }

    /**
     * Gets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @return cost for this enchantment
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost (minimum level) which is displayed as a number on the right
     * hand side of the enchantment offer.
     *
     * @param cost cost for this enchantment
     */
    public void setCost(int cost) {
        Preconditions.checkArgument(cost > 0, "The cost must be greater than 0!");

        this.cost = cost;
    }
}
