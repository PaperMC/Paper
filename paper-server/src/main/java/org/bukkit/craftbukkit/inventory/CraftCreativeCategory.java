package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.world.item.CreativeModeTab;
import org.bukkit.inventory.CreativeCategory;

public final class CraftCreativeCategory {

    private static final Map<CreativeModeTab, CreativeCategory> NMS_TO_BUKKIT = ImmutableMap.<CreativeModeTab, CreativeCategory>builder()
            .put(CreativeModeTab.TAB_BUILDING_BLOCKS, CreativeCategory.BUILDING_BLOCKS)
            .put(CreativeModeTab.TAB_DECORATIONS, CreativeCategory.DECORATIONS)
            .put(CreativeModeTab.TAB_REDSTONE, CreativeCategory.REDSTONE)
            .put(CreativeModeTab.TAB_TRANSPORTATION, CreativeCategory.TRANSPORTATION)
            .put(CreativeModeTab.TAB_MISC, CreativeCategory.MISC) // Interchangeable in NMS
            .put(CreativeModeTab.TAB_MATERIALS, CreativeCategory.MISC) // Interchangeable in NMS
            .put(CreativeModeTab.TAB_FOOD, CreativeCategory.FOOD)
            .put(CreativeModeTab.TAB_TOOLS, CreativeCategory.TOOLS)
            .put(CreativeModeTab.TAB_COMBAT, CreativeCategory.COMBAT)
            .put(CreativeModeTab.TAB_BREWING, CreativeCategory.BREWING)
            .build();

    public static CreativeCategory fromNMS(CreativeModeTab tab) {
        if (!NMS_TO_BUKKIT.containsKey(tab)) {
            throw new UnsupportedOperationException("Item is not present in any known CreativeModeTab. This is a bug.");
        }

        return (tab != null) ? NMS_TO_BUKKIT.get(tab) : null;
    }
}
