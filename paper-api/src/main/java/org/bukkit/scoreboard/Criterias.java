package org.bukkit.scoreboard;

/**
 * Criteria names which trigger an objective to be modified by actions in-game
 */
public class Criterias {
    public static final String HEALTH;
    public static final String PLAYER_KILLS;
    public static final String TOTAL_KILLS;
    public static final String DEATHS;

    static {
        HEALTH="health";
        PLAYER_KILLS="playerKillCount";
        TOTAL_KILLS="totalKillCount";
        DEATHS="deathCount";
    }

    private Criterias() {}
}
