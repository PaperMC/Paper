package org.bukkit.scoreboard;

import net.kyori.adventure.text.format.NamedTextColor; // Paper
/**
 * Locations for displaying objectives to the player
 */
public enum DisplaySlot {
    // Paper start
    BELOW_NAME("below_name"),
    PLAYER_LIST("list"),
    SIDEBAR("sidebar"),
    SIDEBAR_TEAM_BLACK(NamedTextColor.BLACK),
    SIDEBAR_TEAM_DARK_BLUE(NamedTextColor.DARK_BLUE),
    SIDEBAR_TEAM_DARK_GREEN(NamedTextColor.DARK_GREEN),
    SIDEBAR_TEAM_DARK_AQUA(NamedTextColor.DARK_AQUA),
    SIDEBAR_TEAM_DARK_RED(NamedTextColor.DARK_RED),
    SIDEBAR_TEAM_DARK_PURPLE(NamedTextColor.DARK_PURPLE),
    SIDEBAR_TEAM_GOLD(NamedTextColor.GOLD),
    SIDEBAR_TEAM_GRAY(NamedTextColor.GRAY),
    SIDEBAR_TEAM_DARK_GRAY(NamedTextColor.DARK_GRAY),
    SIDEBAR_TEAM_BLUE(NamedTextColor.BLUE),
    SIDEBAR_TEAM_GREEN(NamedTextColor.GREEN),
    SIDEBAR_TEAM_AQUA(NamedTextColor.AQUA),
    SIDEBAR_TEAM_RED(NamedTextColor.RED),
    SIDEBAR_TEAM_LIGHT_PURPLE(NamedTextColor.LIGHT_PURPLE),
    SIDEBAR_TEAM_YELLOW(NamedTextColor.YELLOW),
    SIDEBAR_TEAM_WHITE(NamedTextColor.WHITE);

    public static final net.kyori.adventure.util.Index<String, DisplaySlot> NAMES = net.kyori.adventure.util.Index.create(DisplaySlot.class, DisplaySlot::getId);

    private final String id;

    DisplaySlot(@org.jetbrains.annotations.NotNull String id) {
        this.id = id;
    }

    DisplaySlot(@org.jetbrains.annotations.NotNull NamedTextColor color) {
        this.id = "sidebar.team." + color;
    }

    /**
     * Get the string id of this display slot.
     *
     * @return the string id
     */
    public @org.jetbrains.annotations.NotNull String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.id;
    }
    // Paper end
}
