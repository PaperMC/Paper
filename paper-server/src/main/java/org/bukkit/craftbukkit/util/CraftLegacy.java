package org.bukkit.craftbukkit.util;

import java.util.Arrays;
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

    public static Material[] modern_values() {
        Material[] values = Material.values();
        return Arrays.copyOfRange(values, 0, Material.LEGACY_AIR.ordinal());
    }

    public static int modern_ordinal(Material material) {
        if (material.isLegacy()) {
            // SPIGOT-4002: Fix for eclipse compiler manually compiling in default statements to lookupswitch
            throw new NoSuchFieldError("Legacy field ordinal: " + material);
        }

        return material.ordinal();
    }
}
