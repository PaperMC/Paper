package org.bukkit.inventory;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

/**
 * An item's rarity determines the default color of its name. This enum is
 * ordered from least rare to most rare.
 */
public enum ItemRarity {

    // Start generate - ItemRarity
    // @GeneratedFrom 1.21.6-pre1
    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE);
    // End generate - ItemRarity

    private final net.kyori.adventure.text.format.NamedTextColor color;

    ItemRarity(final net.kyori.adventure.text.format.NamedTextColor color) {
        this.color = color;
    }

    /**
     * Gets the color formatting associated with this rarity.
     *
     * @return the color
     */
    public @org.jetbrains.annotations.NotNull TextColor color() {
        return this.color;
    }
}
