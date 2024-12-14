package org.bukkit.inventory.meta;

import org.jetbrains.annotations.NotNull;

/**
 * Represents armor that an entity can equip and can also be colored.
 *
 * @since 1.19.4
 */
public interface ColorableArmorMeta extends ArmorMeta, LeatherArmorMeta {

    @Override
    @NotNull
    ColorableArmorMeta clone();
}
