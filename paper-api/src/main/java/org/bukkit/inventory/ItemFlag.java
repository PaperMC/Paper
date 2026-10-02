package org.bukkit.inventory;

import io.papermc.paper.datacomponent.DataComponentType;

/// A ItemFlag can hide some Attributes from ItemStacks
/// @apiNote Setting these without also setting the data they are hiding
/// may not result in the item flag being persisted in the ItemMeta/ItemStack.
public enum ItemFlag {

    /// Setting to show/hide enchants
    HIDE_ENCHANTS,
    /// Setting to show/hide Attributes like Damage
    HIDE_ATTRIBUTES,
    /// Setting to show/hide the unbreakable State
    HIDE_UNBREAKABLE,
    /// Setting to show/hide what the ItemStack can break/destroy
    HIDE_DESTROYS,
    /// Setting to show/hide where this ItemStack can be build/placed on
    HIDE_PLACED_ON,
    /// Setting to show/hide item-specific information, including, but not limited to:
    ///
    ///   - Potion effects on potions, tipped arrows, and suspicious stew
    ///   - Enchanted book enchantments
    ///   - Book author and generation
    ///   - Record names
    ///   - Patterns of banners and shields
    ///   - Fish bucket variants
    ///   - Instrument item descriptions (i.e. goat horn sounds)
    ///   - Map data
    ///   - Firework data
    ///   - Crossbow projectile info
    ///   - Bundle fullness
    ///   - Shulker box contents
    ///   - Spawner descriptions
    ///
    /// @see #HIDE_STORED_ENCHANTS HIDE\_STORED\_ENCHANTS for hiding stored enchants (like on enchanted books)
    /// @deprecated does not exist anymore and will not properly work with individually hidden data components; see [io.papermc.paper.datacomponent.item.TooltipDisplay] and [ItemStack#setData(DataComponentType.Valued, Object)]
    @Deprecated
    HIDE_ADDITIONAL_TOOLTIP,
    /// Setting to show/hide dyes from colored leather armor.
    HIDE_DYE,
    /// Setting to show/hide armor trim from armor.
    HIDE_ARMOR_TRIM,
    /// Setting to show/hide stored enchants on an item, such as enchantments
    /// on an enchanted book.
    HIDE_STORED_ENCHANTS,
    ;
}
