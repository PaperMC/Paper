package org.bukkit.scoreboard;

import org.jspecify.annotations.NullMarked;

/**
 * Locations for displaying objectives to the player
 */
@NullMarked
public enum DisplaySlot {
    // Start generate - DisplaySlot
    // @GeneratedFrom 1.21.6-pre1
    PLAYER_LIST("list"),
    SIDEBAR("sidebar"),
    BELOW_NAME("below_name"),
    SIDEBAR_TEAM_BLACK("sidebar.team.black"),
    SIDEBAR_TEAM_DARK_BLUE("sidebar.team.dark_blue"),
    SIDEBAR_TEAM_DARK_GREEN("sidebar.team.dark_green"),
    SIDEBAR_TEAM_DARK_AQUA("sidebar.team.dark_aqua"),
    SIDEBAR_TEAM_DARK_RED("sidebar.team.dark_red"),
    SIDEBAR_TEAM_DARK_PURPLE("sidebar.team.dark_purple"),
    SIDEBAR_TEAM_GOLD("sidebar.team.gold"),
    SIDEBAR_TEAM_GRAY("sidebar.team.gray"),
    SIDEBAR_TEAM_DARK_GRAY("sidebar.team.dark_gray"),
    SIDEBAR_TEAM_BLUE("sidebar.team.blue"),
    SIDEBAR_TEAM_GREEN("sidebar.team.green"),
    SIDEBAR_TEAM_AQUA("sidebar.team.aqua"),
    SIDEBAR_TEAM_RED("sidebar.team.red"),
    SIDEBAR_TEAM_LIGHT_PURPLE("sidebar.team.light_purple"),
    SIDEBAR_TEAM_YELLOW("sidebar.team.yellow"),
    SIDEBAR_TEAM_WHITE("sidebar.team.white");
    // End generate - DisplaySlot

    public static final net.kyori.adventure.util.Index<String, DisplaySlot> NAMES = net.kyori.adventure.util.Index.create(DisplaySlot.class, DisplaySlot::getId);

    private final String id;

    DisplaySlot(String id) {
        this.id = id;
    }

    /**
     * Get the string id of this display slot.
     *
     * @return the string id
     */
    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
