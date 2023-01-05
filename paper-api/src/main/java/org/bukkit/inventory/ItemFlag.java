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
    HIDE_ADDITIONAL_TOOLTIP,
    /**
     * Setting to show/hide dyes from colored leather armor.
     */
    HIDE_DYE,
    /**
     * Setting to show/hide armor trim from armor.
     */
    HIDE_ARMOR_TRIM;
    // Paper start
    /**
     * Setting to show/hide item-specific information, including, but not limited to:
     * <ul>
     *     <li>Potion effects on potions, tipped arrows, and suspicious stew</li>
     *     <li>Enchanted book enchantments</li>
     *     <li>Book author and generation</li>
     *     <li>Record names</li>
     *     <li>Patterns of banners and shields</li>
     *     <li>Fish bucket variants</li>
     *     <li>Instrument item descriptions (i.e. goat horn sounds)</li>
     *     <li>Map data</li>
     *     <li>Firework data</li>
     *     <li>Crossbow projectile info</li>
     *     <li>Bundle fullness</li>
     *     <li>Shulker box contents</li>
     *     <li>Spawner descriptions</li>
     * </ul>
     * @deprecated use {@link #HIDE_ADDITIONAL_TOOLTIP}
     */
    @Deprecated(since = "1.20.5")
    public static final ItemFlag HIDE_ITEM_SPECIFICS = HIDE_ADDITIONAL_TOOLTIP;
    // Paper end
}
