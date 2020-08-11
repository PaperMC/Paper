package org.bukkit.inventory;

/**
 * Represents a category in the creative inventory.
 */
public enum CreativeCategory implements net.kyori.adventure.translation.Translatable { // Paper

    /**
     * An assortment of building blocks including dirt, bricks, planks, ores
     * slabs, etc.
     */
    BUILDING_BLOCKS("buildingBlocks"), // Paper
    /**
     * Blocks and items typically used for decorative purposes including
     * candles, saplings, flora, fauna, fences, walls, carpets, etc.
     */
    DECORATIONS("decorations"), // Paper
    /**
     * Blocks used and associated with redstone contraptions including buttons,
     * levers, pressure plates, redstone components, pistons, etc.
     */
    REDSTONE("redstone"), // Paper
    /**
     * Items pertaining to transportation including minecarts, rails, boats,
     * elytra, etc.
     */
    TRANSPORTATION("transportation"), // Paper
    /**
     * Miscellaneous items and blocks that do not fit into other categories
     * including gems, dyes, spawn eggs, discs, banner patterns, etc.
     */
    MISC("misc"), // Paper
    /**
     * Food items consumable by the player including meats, berries, edible
     * drops from creatures, etc.
     */
    FOOD("food"), // Paper
    /**
     * Equipment items meant for general utility including pickaxes, axes, hoes,
     * flint and steel, and useful enchantment books for said tools.
     */
    TOOLS("tools"), // Paper
    /**
     * Equipment items meant for combat including armor, swords, bows, tipped
     * arrows, and useful enchantment books for said equipment.
     */
    COMBAT("combat"), // Paper
    /**
     * All items related to brewing and potions including all types of potions,
     * their variants, and ingredients to brew them.
     */
    BREWING("brewing"); // Paper
    // Paper start
    private final String translationKey;

    CreativeCategory(String translationKey) {
        this.translationKey = "itemGroup." + translationKey;
    }

    @Override
    public @org.jetbrains.annotations.NotNull String translationKey() {
        return this.translationKey;
    }
    // Paper end

}
