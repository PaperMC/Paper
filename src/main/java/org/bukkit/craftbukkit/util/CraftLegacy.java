package org.bukkit.craftbukkit.util;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

/**
 * @deprecated legacy use only
 */
@Deprecated
public final class CraftLegacy {

    private CraftLegacy() {
        //
    }

    public static Material fromLegacy(Material material) {
        if (material == null || !material.isLegacy()) {
            return material;
        }

        return org.bukkit.craftbukkit.legacy.CraftLegacy.fromLegacy(material);
    }

    public static Material fromLegacy(MaterialData materialData) {
        return org.bukkit.craftbukkit.legacy.CraftLegacy.fromLegacy(materialData);
    }
}
