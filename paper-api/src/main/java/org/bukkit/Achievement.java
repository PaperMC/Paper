package org.bukkit;

/**
 * Represents an achievement, which may be given to players.
 */
public enum Achievement {
    OPEN_INVENTORY,
    MINE_WOOD (OPEN_INVENTORY),
    BUILD_WORKBENCH (MINE_WOOD),
    BUILD_PICKAXE (BUILD_WORKBENCH),
    BUILD_FURNACE (BUILD_PICKAXE),
    ACQUIRE_IRON (BUILD_FURNACE),
    BUILD_HOE (BUILD_WORKBENCH),
    MAKE_BREAD (BUILD_HOE),
    BAKE_CAKE (BUILD_HOE),
    BUILD_BETTER_PICKAXE (BUILD_PICKAXE),
    COOK_FISH (BUILD_FURNACE),
    ON_A_RAIL (ACQUIRE_IRON),
    BUILD_SWORD (BUILD_WORKBENCH),
    KILL_ENEMY (BUILD_SWORD),
    KILL_COW (BUILD_SWORD),
    FLY_PIG (KILL_COW),
    SNIPE_SKELETON (KILL_ENEMY),
    GET_DIAMONDS (ACQUIRE_IRON),
    NETHER_PORTAL (GET_DIAMONDS),
    GHAST_RETURN (NETHER_PORTAL),
    GET_BLAZE_ROD (NETHER_PORTAL),
    BREW_POTION (GET_BLAZE_ROD),
    END_PORTAL (GET_BLAZE_ROD),
    THE_END (END_PORTAL),
    ENCHANTMENTS (GET_DIAMONDS),
    OVERKILL (ENCHANTMENTS),
    BOOKCASE (ENCHANTMENTS),
    EXPLORE_ALL_BIOMES (END_PORTAL),
    SPAWN_WITHER (THE_END),
    KILL_WITHER (SPAWN_WITHER),
    FULL_BEACON (KILL_WITHER),
    BREED_COW (KILL_COW),
    DIAMONDS_TO_YOU (GET_DIAMONDS),
    OVERPOWERED (BUILD_BETTER_PICKAXE)
    ;

    private final Achievement parent;

    private Achievement() {
        parent = null;
    }

    private Achievement(Achievement parent) {
        this.parent = parent;
    }

    /**
     * Returns whether or not this achievement has a parent achievement.
     * 
     * @return whether the achievement has a parent achievement
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Returns the parent achievement of this achievement, or null if none.
     * 
     * @return the parent achievement or null
     */
    public Achievement getParent() {
        return parent;
    }
}
