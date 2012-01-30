package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Represents a countable statistic, which is collected by the client
 */
public enum Statistic {
    DAMAGE_DEALT(2020),
    DAMAGE_TAKEN(2021),
    DEATHS(2022),
    MOB_KILLS(2023),
    PLAYER_KILLS(2024),
    FISH_CAUGHT(2025),
    MINE_BLOCK(16777216, true),
    USE_ITEM(6908288, false),
    BREAK_ITEM(16973824, true);

    private final static Map<Integer, Statistic> BY_ID = Maps.newHashMap();
    private final int id;
    private final boolean isSubstat;
    private final boolean isBlock;

    private Statistic(int id) {
        this(id, false, false);
    }

    private Statistic(int id, boolean isBlock) {
        this(id, true, isBlock);
    }

    private Statistic(int id, boolean isSubstat, boolean isBlock) {
        this.id = id;
        this.isSubstat = isSubstat;
        this.isBlock = isBlock;
    }

    /**
     * Gets the ID for this statistic.
     *
     * @return ID of this statistic
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if this is a substatistic.
     * <p />
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

    /**
     * Gets the statistic associated with the given ID.
     *
     * @param id ID of the statistic to return
     * @return statistic with the given ID
     */
    public static Statistic getById(int id) {
        return BY_ID.get(id);
    }

    static {
        for (Statistic statistic : values()) {
            BY_ID.put(statistic.id, statistic);
        }
    }
}
