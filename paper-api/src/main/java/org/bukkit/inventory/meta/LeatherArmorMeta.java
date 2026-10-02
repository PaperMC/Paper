package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// Represents leather armor ([Material#LEATHER_BOOTS],
/// [Material#LEATHER_LEGGINGS], [Material#LEATHER_CHESTPLATE],
/// [Material#LEATHER_HELMET], [Material#LEATHER_HORSE_ARMOR] or
/// [Material#WOLF_ARMOR]) that can be colored.
public interface LeatherArmorMeta extends ItemMeta {

    /// Gets the color of the armor. If it has not been set otherwise, it will
    /// be [ItemFactory#getDefaultLeatherColor()].
    ///
    /// @return the color of the armor, never null
    /// @apiNote The method yielding [ItemFactory#getDefaultLeatherColor()] is incorrect
    /// for [Material#WOLF_ARMOR] as its default color differs. Generally, it is recommended to check
    /// [#isDyed()] to determine if this leather armor is dyed than to compare this color to the default.
    @NotNull
    Color getColor();

    /// Sets the color of the armor.
    ///
    /// @param color the color to set.
    void setColor(@Nullable Color color);

    @Override
    @NotNull
    LeatherArmorMeta clone();

    // Paper start - Expose #hasColor to leather armor
    /// Checks whether this leather armor is dyed.
    ///
    /// @return whether this leather armor is dyed
    boolean isDyed();
    // Paper end - Expose #hasColor to leather armor
}
