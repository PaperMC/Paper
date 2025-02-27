package io.papermc.paper.inventory;

import io.papermc.paper.registry.keys.CreativeModeTabKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a tab in the creative inventory.
 */
@NullMarked
public interface CreativeModeTab extends Keyed, Translatable, Iterable<ItemStack> {

    /**
     * An assortment of building blocks including dirt, bricks, planks, ores
     * slabs, etc.
     */
    CreativeModeTab BUILDING_BLOCKS = getTab(CreativeModeTabKeys.BUILDING_BLOCKS);

    /**
     * An assortment of blocks which have multiple colors like wool and terracotta.
     */
    CreativeModeTab COLORED_BLOCKS = getTab(CreativeModeTabKeys.COLORED_BLOCKS);

    /**
     * Blocks which appear naturally in worlds.
     */
    CreativeModeTab NATURAL_BLOCKS = getTab(CreativeModeTabKeys.NATURAL_BLOCKS);

    /**
     * Blocks which have a function besides "look".
     */
    CreativeModeTab FUNCTIONAL_BLOCKS = getTab(CreativeModeTabKeys.FUNCTIONAL_BLOCKS);

    /**
     * Blocks used and associated with redstone contraptions including buttons,
     * levers, pressure plates, redstone components, pistons, etc.
     */
    CreativeModeTab REDSTONE_BLOCKS = getTab(CreativeModeTabKeys.REDSTONE_BLOCKS);

    /**
     * Equipment items meant for general utility including pickaxes, axes, hoes,
     * flint and steel, and useful enchantment books for said tools.
     */
    CreativeModeTab TOOLS_AND_UTILITIES = getTab(CreativeModeTabKeys.TOOLS_AND_UTILITIES);

    /**
     * Equipment items meant for combat including armor, swords, bows, tipped
     * arrows, and useful enchantment books for said equipment.
     */
    CreativeModeTab COMBAT = getTab(CreativeModeTabKeys.COMBAT);

    /**
     * Food items consumable by the player including meats, berries, edible
     * drops from creatures, etc.
     */
    CreativeModeTab FOOD_AND_DRINK = getTab(CreativeModeTabKeys.FOOD_AND_DRINKS);

    /**
     * Items which are ingredients in recipes.
     */
    CreativeModeTab INGREDIENTS = getTab(CreativeModeTabKeys.INGREDIENTS);

    /**
     * All mob spawn eggs.
     */
    CreativeModeTab SPAWN_EGGS = getTab(CreativeModeTabKeys.SPAWN_EGGS);

    /**
     * Blocks which, in vanilla, only operators are allowed to use.
     */
    CreativeModeTab OP_BLOCKS = getTab(CreativeModeTabKeys.OP_BLOCKS);

    /*
     * Non-category tabs
     */

    /**
     * The survival inventory tab, does not contain items.
     */
    CreativeModeTab INVENTORY = getTab(CreativeModeTabKeys.INVENTORY);

    /**
     * The saved hotbars tab, does not contain items.
     */
    CreativeModeTab HOTBAR = getTab(CreativeModeTabKeys.HOTBAR);

    /**
     * The search tab, contains all items.
     */
    CreativeModeTab SEARCH = getTab(CreativeModeTabKeys.SEARCH);

    private static CreativeModeTab getTab(Key key) {
        return Registry.CREATIVE_MODE_TAB.getOrThrow(key);
    }

    /**
     * @return The user-friendly display name for this tab.
     */
    Component displayName();

    /**
     * @return The item that is used as the icon in the creative inventory.
     */
    ItemStack iconItem();

    /**
     * @return {@code true} if the scrollbar is visible on the right while this tab is focused.
     */
    boolean scrollbarShown();

    /**
     * @param itemType The item type.
     * @return {@code true} if this tab contains an item with the specified item type.
     */
    boolean containsItemType(final ItemType itemType);

    /**
     * @param itemStack The item stack.
     * @return {@code true} if this tab contains the specified item.
     */
    boolean containsItem(final ItemStack itemStack);

    /**
     * @return The row that this tab is in.
     */
    Row getRow();

    /**
     * @return The tab's type.
     */
    Type getType();

    enum Row {
        TOP,
        BOTTOM
    }

    enum Type {
        CATEGORY,
        INVENTORY,
        HOTBAR,
        SEARCH
    }
}
