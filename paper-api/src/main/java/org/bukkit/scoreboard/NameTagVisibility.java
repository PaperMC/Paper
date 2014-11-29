package org.bukkit.scoreboard;

public enum NameTagVisibility {

    /**
     * Always show the player's nametag.
     */
    ALWAYS,
    /**
     * Never show the player's nametag.
     */
    NEVER,
    /**
     * Show the player's nametag only to his own team members.
     */
    HIDE_FOR_OTHER_TEAMS,
    /**
     * Show the player's nametag only to members of other teams.
     */
    HIDE_FOR_OWN_TEAM;
}
