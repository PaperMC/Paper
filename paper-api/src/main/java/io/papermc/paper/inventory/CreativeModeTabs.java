package io.papermc.paper.inventory;

import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.keys.CreativeModeTabKeys;
import org.bukkit.Registry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CreativeModeTabs {

    /**
     * An assortment of building blocks including dirt, bricks, planks, ores
     * slabs, etc.
     */
    public static final CreativeModeTab BUILDING_BLOCKS = getTab(CreativeModeTabKeys.BUILDING_BLOCKS);

    /**
     * An assortment of blocks which have multiple colors like wool and terracotta.
     */
    public static final CreativeModeTab COLORED_BLOCKS = getTab(CreativeModeTabKeys.COLORED_BLOCKS);

    /**
     * Blocks which appear naturally in worlds.
     */
    public static final CreativeModeTab NATURAL_BLOCKS = getTab(CreativeModeTabKeys.NATURAL_BLOCKS);

    /**
     * Blocks which have a function besides "look".
     */
    public static final CreativeModeTab FUNCTIONAL_BLOCKS = getTab(CreativeModeTabKeys.FUNCTIONAL_BLOCKS);

    /**
     * Blocks used and associated with redstone contraptions including buttons,
     * levers, pressure plates, redstone components, pistons, etc.
     */
    public static final CreativeModeTab REDSTONE_BLOCKS = getTab(CreativeModeTabKeys.REDSTONE_BLOCKS);

    /**
     * Equipment items meant for general utility including pickaxes, axes, hoes, and
     * flint and steel.
     */
    public static final CreativeModeTab TOOLS_AND_UTILITIES = getTab(CreativeModeTabKeys.TOOLS_AND_UTILITIES);

    /**
     * Equipment items meant for combat including armor, swords, bows, and tipped arrows.
     */
    public static final CreativeModeTab COMBAT = getTab(CreativeModeTabKeys.COMBAT);

    /**
     * Food items or potions consumable by the player including meats, berries, edible
     * drops from creatures, etc.
     */
    public static final CreativeModeTab FOOD_AND_DRINKS = getTab(CreativeModeTabKeys.FOOD_AND_DRINKS);

    /**
     * Items which are ingredients in recipes.
     */
    public static final CreativeModeTab INGREDIENTS = getTab(CreativeModeTabKeys.INGREDIENTS);

    /**
     * All mob spawn eggs.
     */
    public static final CreativeModeTab SPAWN_EGGS = getTab(CreativeModeTabKeys.SPAWN_EGGS);

    /**
     * Blocks which, in vanilla, only operators are allowed to use.
     */
    public static final CreativeModeTab OP_BLOCKS = getTab(CreativeModeTabKeys.OP_BLOCKS);

    /*
     * Non-category tabs
     */

    /**
     * The survival inventory tab, does not contain items.
     */
    public static final CreativeModeTab INVENTORY = getTab(CreativeModeTabKeys.INVENTORY);

    /**
     * The saved hotbars tab, does not contain items.
     */
    public static final CreativeModeTab HOTBAR = getTab(CreativeModeTabKeys.HOTBAR);

    /**
     * The search tab, contains all items.
     */
    public static final CreativeModeTab SEARCH = getTab(CreativeModeTabKeys.SEARCH);

    private static CreativeModeTab getTab(TypedKey<CreativeModeTab> typed) {
        return Registry.CREATIVE_MODE_TAB.getOrThrow(typed.key());
    }

    private CreativeModeTabs() {
    }
}
