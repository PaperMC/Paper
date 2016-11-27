package org.bukkit.enchantments;

import org.apache.commons.lang.Validate;

/**
 * A class for the available enchantment offers in the enchantment table.
 */
public class EnchantmentOffer {

    private Enchantment enchantment;
    private int enchantmentLevel;
    private int cost;

    public EnchantmentOffer(Enchantment enchantment, int enchantmentLevel, int cost) {
        this.enchantment = enchantment;
        this.enchantmentLevel = enchantmentLevel;
        this.cost = cost;
    }

    /**
     * Get the type of the enchantment.
     *
     * @return type of enchantment
     */
    public Enchantment getEnchantment() {
        return enchantment;
    }

    /**
     * Sets the type of the enchantment.
     *
     * @param enchantment type of the enchantment
     */
    public void setEnchantment(Enchantment enchantment) {
        Validate.notNull(enchantment, "The enchantment may not be null!");

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
        Validate.isTrue(enchantmentLevel > 0, "The enchantment level must be greater than 0!");

        this.enchantmentLevel = enchantmentLevel;
    }

    /**
     * Gets the cost in experience levels the player has to pay to enchant his
     * item with this enchantment.
     *
     * @return cost for this enchantment
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost in experience levels the player has to pay to enchant his
     * item with this enchantment
     *
     * @param cost cost for this enchantment
     */
    public void setCost(int cost) {
        Validate.isTrue(cost > 0, "The cost must be greater than 0!");

        this.cost = cost;
    }
}
