package org.bukkit.enchantments;

/**
 * Represents the applicable target for a {@link Enchantment}
 */
public enum EnchantmentTarget {
    /**
     * Allows the Enchantment to be placed on all items
     */
    ALL,

    /**
     * Allows the Enchantment to be placed on armor
     */
    ARMOR,

    /**
     * Allows the Enchantment to be placed on feet slot armor
     */
    ARMOR_FEET,

    /**
     * Allows the Enchantment to be placed on leg slot armor
     */
    ARMOR_LEGS,

    /**
     * Allows the Enchantment to be placed on torso slot armor
     */
    ARMOR_TORSO,

    /**
     * Allows the Enchantment to be placed on head slot armor
     */
    ARMOR_HEAD,

    /**
     * Allows the Enchantment to be placed on weapons (swords)
     */
    WEAPON,

    /**
     * Allows the Enchantment to be placed on tools (spades, pickaxe, hoes, axes)
     */
    TOOL;
}
