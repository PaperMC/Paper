package org.bukkit;

/**
 * Represents a countable statistic, which is collected by the client
 */
public enum Statistic {
    DAMAGE_DEALT,
    DAMAGE_TAKEN,
    DEATHS,
    MOB_KILLS,
    PLAYER_KILLS,
    FISH_CAUGHT,
    MINE_BLOCK(true),
    USE_ITEM(false),
    BREAK_ITEM(true);

    private final boolean isSubstat;
    private final boolean isBlock;

    private Statistic() {
        this(false, false);
    }

    private Statistic(boolean isBlock) {
        this(true, isBlock);
    }

    private Statistic(boolean isSubstat, boolean isBlock) {
        this.isSubstat = isSubstat;
        this.isBlock = isBlock;
    }

    /**
     * Checks if this is a substatistic.
     * <p>
     * A substatistic exists in mass for each block or item, depending on {@link #isBlock()}
     *
     * @return true if this is a substatistic
     */
    public boolean isSubstatistic() {
        return isSubstat;
    }

    /**
     * Checks if this is a substatistic dealing with blocks (As opposed to items)
     *
     * @return true if this deals with blocks, false if with items
     */
    public boolean isBlock() {
        return isSubstat && isBlock;
    }
}
