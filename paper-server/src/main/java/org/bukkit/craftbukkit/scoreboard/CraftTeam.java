package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import net.minecraft.world.scores.ScoreboardTeamBase.EnumNameTagVisibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

final class CraftTeam extends CraftScoreboardComponent implements Team {
    private final ScoreboardTeam team;

    CraftTeam(CraftScoreboard scoreboard, ScoreboardTeam team) {
        super(scoreboard);
        this.team = team;
    }

    @Override
    public String getName() {
        checkState();

        return team.getName();
    }

    @Override
    public String getDisplayName() {
        checkState();

        return CraftChatMessage.fromComponent(team.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        checkState();

        team.setDisplayName(CraftChatMessage.fromString(displayName)[0]); // SPIGOT-4112: not nullable
    }

    @Override
    public String getPrefix() {
        checkState();

        return CraftChatMessage.fromComponent(team.getPlayerPrefix());
    }

    @Override
    public void setPrefix(String prefix) {
        Preconditions.checkArgument(prefix != null, "Prefix cannot be null");
        checkState();

        team.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix));
    }

    @Override
    public String getSuffix() {
        checkState();

        return CraftChatMessage.fromComponent(team.getPlayerSuffix());
    }

    @Override
    public void setSuffix(String suffix) {
        Preconditions.checkArgument(suffix != null, "Suffix cannot be null");
        checkState();

        team.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
    }

    @Override
    public ChatColor getColor() {
        checkState();

        return CraftChatMessage.getColor(team.getColor());
    }

    @Override
    public void setColor(ChatColor color) {
        Preconditions.checkArgument(color != null, "Color cannot be null");
        checkState();

        team.setColor(CraftChatMessage.getColor(color));
    }

    @Override
    public boolean allowFriendlyFire() {
        checkState();

        return team.isAllowFriendlyFire();
    }

    @Override
    public void setAllowFriendlyFire(boolean enabled) {
        checkState();

        team.setAllowFriendlyFire(enabled);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        checkState();

        return team.canSeeFriendlyInvisibles();
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) {
        checkState();

        team.setSeeFriendlyInvisibles(enabled);
    }

    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        checkState();

        return notchToBukkit(team.getNameTagVisibility());
    }

    @Override
    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        checkState();

        team.setNameTagVisibility(bukkitToNotch(visibility));
    }

    @Override
    public Set<OfflinePlayer> getPlayers() {
        checkState();

        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (String playerName : team.getPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName));
        }
        return players.build();
    }

    @Override
    public Set<String> getEntries() {
        checkState();

        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (String playerName : team.getPlayers()) {
            entries.add(playerName);
        }
        return entries.build();
    }

    @Override
    public int getSize() {
        checkState();

        return team.getPlayers().size();
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        addEntry(player.getName());
    }

    @Override
    public void addEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.addPlayerToTeam(entry, team);
    }

    @Override
    public boolean removePlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return removeEntry(player.getName());
    }

    @Override
    public boolean removeEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        CraftScoreboard scoreboard = checkState();

        if (!team.getPlayers().contains(entry)) {
            return false;
        }

        scoreboard.board.removePlayerFromTeam(entry, team);
        return true;
    }

    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return hasEntry(player.getName());
    }

    @Override
    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        checkState();

        return team.getPlayers().contains(entry);
    }

    @Override
    public void unregister() {
        CraftScoreboard scoreboard = checkState();

        scoreboard.board.removePlayerTeam(team);
    }

    @Override
    public OptionStatus getOption(Option option) {
        checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY:
                return OptionStatus.values()[team.getNameTagVisibility().ordinal()];
            case DEATH_MESSAGE_VISIBILITY:
                return OptionStatus.values()[team.getDeathMessageVisibility().ordinal()];
            case COLLISION_RULE:
                return OptionStatus.values()[team.getCollisionRule().ordinal()];
            default:
                throw new IllegalArgumentException("Unrecognised option " + option);
        }
    }

    @Override
    public void setOption(Option option, OptionStatus status) {
        checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY:
                team.setNameTagVisibility(EnumNameTagVisibility.values()[status.ordinal()]);
                break;
            case DEATH_MESSAGE_VISIBILITY:
                team.setDeathMessageVisibility(EnumNameTagVisibility.values()[status.ordinal()]);
                break;
            case COLLISION_RULE:
                team.setCollisionRule(ScoreboardTeamBase.EnumTeamPush.values()[status.ordinal()]);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised option " + option);
        }
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
    CraftScoreboard checkState() {
        Preconditions.checkState(getScoreboard().board.getPlayerTeam(team.getName()) != null, "Unregistered scoreboard component");

        return getScoreboard();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.team != null ? this.team.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftTeam other = (CraftTeam) obj;
        return !(this.team != other.team && (this.team == null || !this.team.equals(other.team)));
    }

}
