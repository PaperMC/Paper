package io.papermc.paper.inventory;

import com.google.common.base.Enums;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import org.bukkit.inventory.CreativeCategory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaperCreativeCategory {
    public static final Collection<CreativeModeTab> CATEGORIES = CreativeModeTabs.streamAllTabs().filter(tab -> tab.getType() == CreativeModeTab.Type.CATEGORY).toList();

    private static final Map<CreativeModeTab, CreativeCategory> internalToAPI = Util.make(ImmutableMap.<CreativeModeTab, CreativeCategory>builder(), map -> {
        final Map<String, CreativeCategory> creativeCategoryByKey = new HashMap<>();
        for (final CreativeCategory category : CreativeCategory.values()) {
            if (Enums.getField(category).isAnnotationPresent(Deprecated.class))
                continue;

            creativeCategoryByKey.put(category.translationKey(), category);
        }

        for (final CreativeModeTab category : CATEGORIES) {
            final String key = ((TranslatableContents) category.getDisplayName().getContents()).getKey();

            final CreativeCategory apiCategory = creativeCategoryByKey.get(key);
            if (apiCategory != null) {
                map.put(category, apiCategory);
            }
        }
    }).build();

    public static final Map<CreativeModeTab, Collection<ItemStack>> CATEGORY_CONTENTS = Util.make(ImmutableMap.<CreativeModeTab, Collection<ItemStack>>builder(), map -> {
        // Based on CreativeModeTab#buildContents to obtain the items within each category.
        final FeatureFlagSet featureFlags = MinecraftServer.getServer().getWorldData().enabledFeatures();

        // Parameters to pass to the display item generator, true is passed for hasPermissions to include operator blocks.
        final CreativeModeTab.ItemDisplayParameters parameters = new CreativeModeTab.ItemDisplayParameters(featureFlags, true, MinecraftServer.getServer().registryAccess());

        for (final CreativeModeTab category : CATEGORIES) {
            CreativeModeTab.ItemDisplayBuilder itemDisplayBuilder = new CreativeModeTab.ItemDisplayBuilder(category, featureFlags);

            category.displayItemsGenerator.accept(parameters, itemDisplayBuilder);

            // Combine all items into a single set of items
            final Set<ItemStack> items = Stream.concat(itemDisplayBuilder.searchTabContents.stream(), itemDisplayBuilder.tabContents.stream())
                .collect(Collectors.toCollection(ItemStackLinkedSet::createTypeAndComponentsSet));

            map.put(category, items);
        }
    }).build();

    private PaperCreativeCategory() {}

    public static CreativeCategory fromNms(CreativeModeTab tab) {
        return internalToAPI.get(tab);
    }
}
