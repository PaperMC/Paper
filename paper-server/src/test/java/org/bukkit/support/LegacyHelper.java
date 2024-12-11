package org.bukkit.support;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public final class LegacyHelper {

    // Materials that only exist in block form (or are legacy)
    private static final List<Material> INVALIDATED_MATERIALS;

    static {
        ImmutableList.Builder<Material> builder = ImmutableList.builder();
        for (Material m : Material.values()) {
            if (m.isLegacy() || CraftMagicNumbers.getItem(m) == null) {
                builder.add(m);
            }
        }
        INVALIDATED_MATERIALS = builder.build();
    }

    private LegacyHelper() {
    }

    public static List<Material> getInvalidatedMaterials() {
        return LegacyHelper.INVALIDATED_MATERIALS;
    }
}
