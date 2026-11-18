package io.papermc.paper.inventory;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

/**
 * Represents the rarity of an item.
 *
 * @deprecated use {@link org.bukkit.inventory.ItemRarity} with {@link org.bukkit.inventory.meta.ItemMeta#getRarity()}
 */
@Deprecated(forRemoval = true, since = "1.20.5")
public enum ItemRarity {

    COMMON(NamedTextColor.WHITE),
    UNCOMMON(NamedTextColor.YELLOW),
    RARE(NamedTextColor.AQUA),
    EPIC(NamedTextColor.LIGHT_PURPLE);

    private final TextColor color;

    ItemRarity(final TextColor color) {
        this.color = color;
    }

    /**
     * Gets the color formatting associated with the rarity.
     *
     * @return the color of the rarity
     */
    public TextColor getColor() {
        return this.color;
    }
}
