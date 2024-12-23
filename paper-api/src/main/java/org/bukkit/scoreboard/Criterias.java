package org.bukkit.scoreboard;

/**
 * Criteria names which trigger an objective to be modified by actions in-game
 *
 * @deprecated use the constants declared in {@link Criteria} instead
 * @since 1.5.1
 */
@Deprecated(since = "1.19.2")
public final class Criterias {

    public static final String HEALTH = "health";
    public static final String PLAYER_KILLS = "playerKillCount";
    public static final String TOTAL_KILLS = "totalKillCount";
    public static final String DEATHS = "deathCount";

    private Criterias() {}
}
