package org.bukkit.craftbukkit.scoreboard;

import java.util.Set;

import net.minecraft.server.EnumNameTagVisibility;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.ImmutableSet;

import net.minecraft.server.ScoreboardTeam;

final class CraftTeam extends CraftScoreboardComponent implements Team {
    private final ScoreboardTeam team;

    CraftTeam(CraftScoreboard scoreboard, ScoreboardTeam team) {
        super(scoreboard);
        this.team = team;
    }

    public String getName() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.getName();
    }

    public String getDisplayName() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.getDisplayName();
    }

    public void setDisplayName(String displayName) throws IllegalStateException {
        Validate.notNull(displayName, "Display name cannot be null");
        Validate.isTrue(displayName.length() <= 32, "Display name '" + displayName + "' is longer than the limit of 32 characters");
        CraftScoreboard scoreboard = checkState();

        team.setDisplayName(displayName);
    }

    public String getPrefix() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.getPrefix();
    }

    public void setPrefix(String prefix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(prefix, "Prefix cannot be null");
        Validate.isTrue(prefix.length() <= 32, "Prefix '" + prefix + "' is longer than the limit of 32 characters");
        CraftScoreboard scoreboard = checkState();

        team.setPrefix(prefix);
    }

    public String getSuffix() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.getSuffix();
    }

    public void setSuffix(String suffix) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(suffix, "Suffix cannot be null");
        Validate.isTrue(suffix.length() <= 32, "Suffix '" + suffix + "' is longer than the limit of 32 characters");
        CraftScoreboard scoreboard = checkState();

        team.setSuffix(suffix);
    }

    public boolean allowFriendlyFire() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.allowFriendlyFire();
    }

    public void setAllowFriendlyFire(boolean enabled) throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        team.setAllowFriendlyFire(enabled);
    }

    public boolean canSeeFriendlyInvisibles() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.canSeeFriendlyInvisibles();
    }

    public void setCanSeeFriendlyInvisibles(boolean enabled) throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        team.setCanSeeFriendlyInvisibles(enabled);
    }

    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        CraftScoreboard scoreboard = checkState();

        // PAIL: Rename
        return notchToBukkit(team.i()); // i is sent in PacketPlayOutScoreboardTeam, cannot see a use of j
    }

    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        CraftScoreboard scoreboard = checkState();

        // PAIL: Rename
        team.a(bukkitToNotch(visibility)); // i is sent in PacketPlayOutScoreboardTeam, cannot see a use of j
    }

    public Set<OfflinePlayer> getPlayers() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (Object o : team.getPlayerNameSet()) {
            players.add(Bukkit.getOfflinePlayer(o.toString()));
        }
        return players.build();
    }

    public int getSize() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        return team.getPlayerNameSet().size();
    }

    public void addPlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.addPlayerToTeam(player.getName(), team.getName());
    }

    public boolean removePlayer(OfflinePlayer player) throws IllegalStateException, IllegalArgumentException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        CraftScoreboard scoreboard = checkState();

        if (!team.getPlayerNameSet().contains(player.getName())) {
            return false;
        }

        scoreboard.board.removePlayerFromTeam(player.getName(), team);
        return true;
    }

    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Validate.notNull(player, "OfflinePlayer cannot be null");
        CraftScoreboard scoreboard = checkState();

        return team.getPlayerNameSet().contains(player.getName());
    }

    @Override
    public void unregister() throws IllegalStateException {
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.removeTeam(team);
    }

    public static EnumNameTagVisibility bukkitToNotch(NameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return EnumNameTagVisibility.ALWAYS;
            case NEVER:
                return EnumNameTagVisibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return EnumNameTagVisibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    public static NameTagVisibility notchToBukkit(EnumNameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return NameTagVisibility.ALWAYS;
            case NEVER:
                return NameTagVisibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return NameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return NameTagVisibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    @Override
    CraftScoreboard checkState() throws IllegalStateException {
        if (getScoreboard().board.getTeam(team.getName()) == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        }

        return getScoreboard();
    }
}
