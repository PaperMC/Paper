package org.bukkit.inventory;

import io.papermc.paper.inventory.CreativeModeTab;

/**
 * Represents a category in the creative inventory.
 *
 * @deprecated Replaced by {@link CreativeModeTab}
 */
@Deprecated(since = "1.21.11")
public enum CreativeCategory implements net.kyori.adventure.translation.Translatable { // Paper

    /**
     * An assortment of building blocks including dirt, bricks, planks, ores
     * slabs, etc.
     */
    BUILDING_BLOCKS("buildingBlocks"),

    /**
     * An assortment of blocks which have multiple colors like wool and terracotta.
     */
    COLORED_BLOCKS("coloredBlocks"),

    /**
     * Blocks which appear naturally in worlds.
     */
    NATURAL_BLOCKS("natural"),

    /**
     * Blocks which have a function besides "look".
     */
    FUNCTIONAL_BLOCKS("functional"),

    /**
     * Items which are ingredients in recipes.
     */
    INGREDIENTS("ingredients"),

    /**
     * All mob spawn eggs.
     */
    SPAWN_EGGS("spawnEggs"),

    /**
     * Blocks which in vanilla, only operators are allowed to use.
     */
    OP_BLOCKS("op"),

    /**
     * Blocks and items typically used for decorative purposes including
     * candles, saplings, flora, fauna, fences, walls, carpets, etc.
     *
     * @deprecated removed in 1.19.3
     */
    @Deprecated(since = "1.19.3", forRemoval = true)
    DECORATIONS("decorations"),
    /**
     * Blocks used and associated with redstone contraptions including buttons,
     * levers, pressure plates, redstone components, pistons, etc.
     */
    REDSTONE("redstone"),
    /**
     * Items pertaining to transportation including minecarts, rails, boats,
     * elytra, etc.
     *
     * @deprecated removed in 1.19.3
     */
    @Deprecated(since = "1.19.3", forRemoval = true)
    TRANSPORTATION("transportation"),
    /**
     * Miscellaneous items and blocks that do not fit into other categories
     * including gems, dyes, spawn eggs, discs, banner patterns, etc.
     *
     * @deprecated removed in 1.19.3
     */
    @Deprecated(since = "1.19.3", forRemoval = true)
    MISC("misc"),
    /**
     * Food items or potions consumable by the player including meats, berries, edible
     * drops from creatures, etc.
     */
    FOOD("foodAndDrink"),
    /**
     * Equipment items meant for general utility including pickaxes, axes, hoes, and
     * flint and steel.
     */
    TOOLS("tools"),
    /**
     * Equipment items meant for combat including armor, swords, bows, and tipped arrows.
     */
    COMBAT("combat"),
    /**
     * All items related to brewing and potions including all types of potions,
     * their variants, and ingredients to brew them.
     *
     * @deprecated removed in 1.19.3
     */
    @Deprecated(since = "1.19.3", forRemoval = true)
    BREWING("brewing");

    private final String translationKey;

    CreativeCategory(String translationKey) {
        this.translationKey = "itemGroup." + translationKey;
    }

    @Override
    public @org.jetbrains.annotations.NotNull String translationKey() {
        return this.translationKey;
    }

}
