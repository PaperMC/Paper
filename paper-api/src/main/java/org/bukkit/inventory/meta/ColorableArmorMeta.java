package org.bukkit.inventory.meta;

import org.jetbrains.annotations.NotNull;

/**
 * Represents armor that an entity can equip and can also be colored.
 */
public interface ColorableArmorMeta extends ArmorMeta, LeatherArmorMeta {

    @Override
    @NotNull
    ColorableArmorMeta clone();
}
