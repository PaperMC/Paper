package org.bukkit.inventory;

/**
 * A ItemFlag can hide some Attributes from ItemStacks
 */
public enum ItemFlag {

    /**
     * Setting to show/hide enchants
     */
    HIDE_ENCHANTS,
    /**
     * Setting to show/hide Attributes like Damage
     */
    HIDE_ATTRIBUTES,
    /**
     * Setting to show/hide the unbreakable State
     */
    HIDE_UNBREAKABLE,
    /**
     * Setting to show/hide what the ItemStack can break/destroy
     */
    HIDE_DESTROYS,
    /**
     * Setting to show/hide where this ItemStack can be build/placed on
     */
    HIDE_PLACED_ON,
    /**
     * Setting to show/hide potion effects, book and firework information, map
     * tooltips, patterns of banners, and enchantments of enchanted books.
     */
    HIDE_POTION_EFFECTS,
    /**
     * Setting to show/hide dyes from colored leather armor.
     */
    HIDE_DYE,
    /**
     * Setting to show/hide armor trim from leather armor.
     */
    HIDE_ARMOR_TRIM;
}
