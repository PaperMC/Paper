package io.papermc.paper.enchantments;

/**
 * @deprecated Enchantments do not have a "rarity" since 1.20.5
 */
@Deprecated(forRemoval = true, since = "1.20.5")
public enum EnchantmentRarity {

    COMMON(10),
    UNCOMMON(5),
    RARE(2),
    VERY_RARE(1);

    private final int weight;

    EnchantmentRarity(int weight) {
        this.weight = weight;
    }

    /**
     * Gets the weight for the rarity.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }
}
