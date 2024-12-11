package org.bukkit.craftbukkit.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

final class CraftTeam extends CraftScoreboardComponent implements Team {
    private final PlayerTeam team;

    CraftTeam(CraftScoreboard scoreboard, PlayerTeam team) {
        super(scoreboard);
        this.team = team;
    }

    @Override
    public String getName() {
        this.checkState();

        return this.team.getName();
    }

    @Override
    public String getDisplayName() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.team.getDisplayName());
    }

    @Override
    public void setDisplayName(String displayName) {
        Preconditions.checkArgument(displayName != null, "Display name cannot be null");
        this.checkState();

        this.team.setDisplayName(CraftChatMessage.fromString(displayName)[0]); // SPIGOT-4112: not nullable
    }

    @Override
    public String getPrefix() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.team.getPlayerPrefix());
    }

    @Override
    public void setPrefix(String prefix) {
        Preconditions.checkArgument(prefix != null, "Prefix cannot be null");
        this.checkState();

        this.team.setPlayerPrefix(CraftChatMessage.fromStringOrNull(prefix));
    }

    @Override
    public String getSuffix() {
        this.checkState();

        return CraftChatMessage.fromComponent(this.team.getPlayerSuffix());
    }

    @Override
    public void setSuffix(String suffix) {
        Preconditions.checkArgument(suffix != null, "Suffix cannot be null");
        this.checkState();

        this.team.setPlayerSuffix(CraftChatMessage.fromStringOrNull(suffix));
    }

    @Override
    public ChatColor getColor() {
        this.checkState();

        return CraftChatMessage.getColor(this.team.getColor());
    }

    @Override
    public void setColor(ChatColor color) {
        Preconditions.checkArgument(color != null, "Color cannot be null");
        this.checkState();

        this.team.setColor(CraftChatMessage.getColor(color));
    }

    @Override
    public boolean allowFriendlyFire() {
        this.checkState();

        return this.team.isAllowFriendlyFire();
    }

    @Override
    public void setAllowFriendlyFire(boolean enabled) {
        this.checkState();

        this.team.setAllowFriendlyFire(enabled);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        this.checkState();

        return this.team.canSeeFriendlyInvisibles();
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean enabled) {
        this.checkState();

        this.team.setSeeFriendlyInvisibles(enabled);
    }

    @Override
    public NameTagVisibility getNameTagVisibility() throws IllegalArgumentException {
        this.checkState();

        return CraftTeam.notchToBukkit(this.team.getNameTagVisibility());
    }

    @Override
    public void setNameTagVisibility(NameTagVisibility visibility) throws IllegalArgumentException {
        this.checkState();

        this.team.setNameTagVisibility(CraftTeam.bukkitToNotch(visibility));
    }

    @Override
    public Set<OfflinePlayer> getPlayers() {
        this.checkState();

        ImmutableSet.Builder<OfflinePlayer> players = ImmutableSet.builder();
        for (String playerName : this.team.getPlayers()) {
            players.add(Bukkit.getOfflinePlayer(playerName));
        }
        return players.build();
    }

    @Override
    public Set<String> getEntries() {
        this.checkState();

        ImmutableSet.Builder<String> entries = ImmutableSet.builder();
        for (String playerName : this.team.getPlayers()) {
            entries.add(playerName);
        }
        return entries.build();
    }

    @Override
    public int getSize() {
        this.checkState();

        return this.team.getPlayers().size();
    }

    @Override
    public void addPlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        this.addEntry(player.getName());
    }

    @Override
    public void addEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        CraftScoreboard scoreboard = this.checkState();

        scoreboard.board.addPlayerToTeam(entry, this.team);
    }

    @Override
    public boolean removePlayer(OfflinePlayer player) {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return this.removeEntry(player.getName());
    }

    @Override
    public boolean removeEntry(String entry) {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        CraftScoreboard scoreboard = this.checkState();

        if (!this.team.getPlayers().contains(entry)) {
            return false;
        }

        scoreboard.board.removePlayerFromTeam(entry, this.team);
        return true;
    }

    @Override
    public boolean hasPlayer(OfflinePlayer player) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(player != null, "OfflinePlayer cannot be null");
        return this.hasEntry(player.getName());
    }

    @Override
    public boolean hasEntry(String entry) throws IllegalArgumentException, IllegalStateException {
        Preconditions.checkArgument(entry != null, "Entry cannot be null");
        this.checkState();

        return this.team.getPlayers().contains(entry);
    }

    @Override
    public void unregister() {
        CraftScoreboard scoreboard = this.checkState();

        scoreboard.board.removePlayerTeam(this.team);
    }

    @Override
    public OptionStatus getOption(Option option) {
        this.checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY:
                return OptionStatus.values()[this.team.getNameTagVisibility().ordinal()];
            case DEATH_MESSAGE_VISIBILITY:
                return OptionStatus.values()[this.team.getDeathMessageVisibility().ordinal()];
            case COLLISION_RULE:
                return OptionStatus.values()[this.team.getCollisionRule().ordinal()];
            default:
                throw new IllegalArgumentException("Unrecognised option " + option);
        }
    }

    @Override
    public void setOption(Option option, OptionStatus status) {
        this.checkState();

        switch (option) {
            case NAME_TAG_VISIBILITY:
                this.team.setNameTagVisibility(Visibility.values()[status.ordinal()]);
                break;
            case DEATH_MESSAGE_VISIBILITY:
                this.team.setDeathMessageVisibility(Visibility.values()[status.ordinal()]);
                break;
            case COLLISION_RULE:
                this.team.setCollisionRule(net.minecraft.world.scores.Team.CollisionRule.values()[status.ordinal()]);
                break;
            default:
                throw new IllegalArgumentException("Unrecognised option " + option);
        }
    }

    public static Visibility bukkitToNotch(NameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return Visibility.ALWAYS;
            case NEVER:
                return Visibility.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return Visibility.HIDE_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return Visibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalArgumentException("Unknown visibility level " + visibility);
        }
    }

    public static NameTagVisibility notchToBukkit(Visibility visibility) {
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
        Preconditions.checkState(this.getScoreboard().board.getPlayerTeam(this.team.getName()) != null, "Unregistered scoreboard component");

        return this.getScoreboard();
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftTeam other = (CraftTeam) obj;
        return !(this.team != other.team && (this.team == null || !this.team.equals(other.team)));
    }

}
