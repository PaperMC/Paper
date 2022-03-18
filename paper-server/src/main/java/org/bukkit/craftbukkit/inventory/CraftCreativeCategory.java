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
            .put(CreativeModeTab.TAB_MISC, CreativeCategory.MISC) // Interchangeable with TAB_MATERIALS, same instance
            .put(CreativeModeTab.TAB_FOOD, CreativeCategory.FOOD)
            .put(CreativeModeTab.TAB_TOOLS, CreativeCategory.TOOLS)
            .put(CreativeModeTab.TAB_COMBAT, CreativeCategory.COMBAT)
            .put(CreativeModeTab.TAB_BREWING, CreativeCategory.BREWING)
            .build();

    public static CreativeCategory fromNMS(CreativeModeTab tab) {
        if (tab == null) {
            return null;
        }

        CreativeCategory bukkit = NMS_TO_BUKKIT.get(tab);
        if (bukkit == null) {
            throw new UnsupportedOperationException("Item is not present in any known CreativeModeTab. This is a bug.");
        }

        return bukkit;
    }
}
