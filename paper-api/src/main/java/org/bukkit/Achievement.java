package org.bukkit;

/**
 * Represents an achievement, which may be given to players
 */
public enum Achievement {
    OPEN_INVENTORY,
    MINE_WOOD,
    BUILD_WORKBENCH,
    BUILD_PICKAXE,
    BUILD_FURNACE,
    ACQUIRE_IRON,
    BUILD_HOE,
    MAKE_BREAD,
    BAKE_CAKE,
    BUILD_BETTER_PICKAXE,
    COOK_FISH,
    ON_A_RAIL,
    BUILD_SWORD,
    KILL_ENEMY,
    KILL_COW,
    FLY_PIG,
    SNIPE_SKELETON,
    GET_DIAMONDS,
    NETHER_PORTAL,
    GHAST_RETURN,
    GET_BLAZE_ROD,
    BREW_POTION,
    END_PORTAL,
    THE_END,
    ENCHANTMENTS,
    OVERKILL,
    BOOKCASE,
    BREED_COW,
    SPAWN_WITHER,
    KILL_WITHER,
    FULL_BEACON,
    EXPLORE_ALL_BIOMES,
    DIAMONDS_TO_YOU,
    ;

    /**
     * The offset used to distinguish Achievements and Statistics
     * @deprecated Magic value
     */
    @Deprecated
    public final static int STATISTIC_OFFSET = 0x500000;
}
